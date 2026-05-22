package cz.muni.fi.pa165.customerservice.config;

import cz.muni.fi.pa165.commonservice.config.SecurityScopes;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.Scopes;
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
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/actuator/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/customers/**").access(hasScope(SecurityScopes.READ_SCOPE))
                .requestMatchers(HttpMethod.POST, "/customers/**").access(hasScope(SecurityScopes.WRITE_SCOPE))
                .requestMatchers(HttpMethod.PUT, "/customers/**").access(hasScope(SecurityScopes.WRITE_SCOPE))
                .requestMatchers(HttpMethod.DELETE, "/customers/**").access(hasScope(SecurityScopes.WRITE_SCOPE))

                .requestMatchers(HttpMethod.GET, "/purchases/**").access(hasScope(SecurityScopes.READ_SCOPE))
                .requestMatchers(HttpMethod.POST, "/purchases/**").access(hasScope(SecurityScopes.WRITE_SCOPE))
                .requestMatchers(HttpMethod.PUT, "/purchases/**").access(hasScope(SecurityScopes.WRITE_SCOPE))
                .requestMatchers(HttpMethod.DELETE, "/purchases/**").access(hasScope(SecurityScopes.WRITE_SCOPE))

                .requestMatchers(HttpMethod.GET, "/reviews/**").access(hasScope(SecurityScopes.READ_SCOPE))
                .requestMatchers(HttpMethod.POST, "/reviews/**").access(hasScope(SecurityScopes.WRITE_SCOPE))
                .requestMatchers(HttpMethod.PUT, "/reviews/**").access(hasScope(SecurityScopes.WRITE_SCOPE))
                .requestMatchers(HttpMethod.DELETE, "/reviews/**").access(hasScope(SecurityScopes.WRITE_SCOPE))
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
        String issuer = Objects.requireNonNull(properties.getJwt().getIssuerUri());
        String audience = properties.getJwt().getAudiences().get(0);
        OAuth2TokenValidator<Jwt> validator = JwtValidators.createAtJwtValidator()
            .clientId(audience)
            .audience(audience)
            .issuer(issuer)
            .build();
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withIssuerLocation(issuer).build();
        decoder.setJwtValidator(validator);
        return decoder;
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer(){
        return openApi -> {
            openApi.getComponents().addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .description("OAuth 2 Authorization Code Grant")
                    .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                            .authorizationUrl("https://id.muni.cz/oidc/authorize")
                            .tokenUrl("https://id.muni.cz/oidc/token")
                            .scopes(new Scopes()
                                .addString("openid", "user identifier")
                                .addString(SecurityScopes.READ_SCOPE,  "read customer data")
                                .addString(SecurityScopes.WRITE_SCOPE, "create, update and delete customer data")
                            )
                        )
                    )

            );

        };
    }
}
