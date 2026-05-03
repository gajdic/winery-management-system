package cz.muni.fi.pa165.inventoryservice.config;

import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
public class SecurityConfig {

    public static final String SECURITY_SCHEME_NAME = "MUNI";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(x -> x
                .requestMatchers(
                    "/swagger-ui.html", "/swagger-ui/**",
                    "/v1/api-docs/**", "/v3/api-docs/**",
                    "/h2-console/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/stocks/**").access(hasScope("test_read"))
                .requestMatchers(HttpMethod.GET, "/reservations/**").access(hasScope("test_read"))
                .requestMatchers(HttpMethod.POST,   "/stocks/**").access(hasScope("test_write"))
                .requestMatchers(HttpMethod.PUT,    "/stocks/**").access(hasScope("test_write"))
                .requestMatchers(HttpMethod.DELETE, "/stocks/**").access(hasScope("test_write"))
                .requestMatchers(HttpMethod.POST,   "/reservations/**").access(hasScope("test_write"))
                .requestMatchers(HttpMethod.PUT,    "/reservations/**").access(hasScope("test_write"))
                .requestMatchers(HttpMethod.DELETE, "/reservations/**").access(hasScope("test_write"))
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties props) {
        String issuer = Objects.requireNonNull(props.getJwt().getIssuerUri());
        String audience = props.getJwt().getAudiences().get(0);
        OAuth2TokenValidator<Jwt> atJwtValidator = JwtValidators.createAtJwtValidator()
            .clientId(audience)
            .audience(audience)
            .issuer(issuer)
            .build();
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withIssuerLocation(issuer).build();
        decoder.setJwtValidator(atJwtValidator);
        return decoder;
    }

    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi -> openApi.getComponents()
            .addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .description("OAuth 2 Authorization Code Grant (MUNI)")
                    .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                            .authorizationUrl("https://id.muni.cz/oidc/authorize")
                            .tokenUrl("https://id.muni.cz/oidc/token")
                            .scopes(new Scopes()
                                .addString("openid", "user identifier")
                                .addString("test_read",  "read stocks and reservations")
                                .addString("test_write", "create / update / delete")
                            )
                        )
                    )
            );
    }
}
