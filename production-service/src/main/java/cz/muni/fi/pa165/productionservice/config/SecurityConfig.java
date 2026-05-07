package cz.muni.fi.pa165.productionservice.config;

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

    public static final String READ_SCOPE = "test_read";
    public static final String WRITE_SCOPE = "test_write";

    public static final String PRODUCTION_API = "/production/**";
    public static final String RECIPES_ENDPOINTS = "/production/recipes/**";
    public static final String BATCHES_ENDPOINTS = "/production/batches/**";
    public static final String PLANNING_ENDPOINT = "/production/planning";
    public static final String QUALITY_EVALUATIONS_ENDPOINT = "/production/quality-evaluations";

    public static final String ERROR_ENDPOINT = "/error";
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    public static final String SWAGGER_UI_ENDPOINTS = "/swagger-ui/**";
    public static final String OPENAPI_ENDPOINTS = "/v3/api-docs/**";

    private static final String[] PUBLIC_ENDPOINTS = {
        ERROR_ENDPOINT,
        SWAGGER_UI_HTML,
        SWAGGER_UI_ENDPOINTS,
        OPENAPI_ENDPOINTS
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.GET, PRODUCTION_API).access(hasScope(READ_SCOPE))
                .requestMatchers(HttpMethod.POST, PLANNING_ENDPOINT, QUALITY_EVALUATIONS_ENDPOINT)
                .access(hasScope(READ_SCOPE))
                .requestMatchers(HttpMethod.POST, RECIPES_ENDPOINTS, BATCHES_ENDPOINTS).access(hasScope(WRITE_SCOPE))
                .requestMatchers(HttpMethod.PUT, PRODUCTION_API).access(hasScope(WRITE_SCOPE))
                .requestMatchers(HttpMethod.PATCH, PRODUCTION_API).access(hasScope(WRITE_SCOPE))
                .requestMatchers(HttpMethod.DELETE, PRODUCTION_API).access(hasScope(WRITE_SCOPE))
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
    public OpenApiCustomizer productionSecurityOpenApiCustomizer() {
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
                                .addString(READ_SCOPE, "read production data and calculate plans")
                                .addString(WRITE_SCOPE, "create, update, and delete production data")
                            )
                        )
                    )
            );
        };
    }
}
