package cz.muni.fi.pa165.harvestservice.config;

import cz.muni.fi.pa165.commonservice.openapi.SwaggerConstants;
import io.swagger.v3.oas.models.Components;
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

    private static final String[] HARVEST_ENDPOINTS = {
        "/vineyards/**",
        "/grape-types/**",
        "/harvest-records/**"
    };

    private static final String[] PUBLIC_ENDPOINTS = {
        "/error",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/actuator/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.GET, HARVEST_ENDPOINTS).access(hasScope(SwaggerConstants.READ_SCOPE))
                .requestMatchers(HttpMethod.POST, HARVEST_ENDPOINTS).access(hasScope(SwaggerConstants.WRITE_SCOPE))
                .requestMatchers(HttpMethod.PUT, HARVEST_ENDPOINTS).access(hasScope(SwaggerConstants.WRITE_SCOPE))
                .requestMatchers(HttpMethod.DELETE, HARVEST_ENDPOINTS).access(hasScope(SwaggerConstants.WRITE_SCOPE))
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
    public OpenApiCustomizer harvestSecurityOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getComponents() == null) {
                openApi.setComponents(new Components());
            }
            openApi.getComponents().addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .description("OAuth 2 Authorization Code Grant using MUNI identity provider")
                    .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                            .authorizationUrl("https://id.muni.cz/oidc/authorize")
                            .tokenUrl("https://id.muni.cz/oidc/token")
                            .scopes(new Scopes()
                                .addString("openid", "get user identifier")
                                .addString(SwaggerConstants.READ_SCOPE, "read harvest data")
                                .addString(SwaggerConstants.WRITE_SCOPE, "create, update, and delete harvest data")
                            )
                        )
                    )
            );
        };
    }
}
