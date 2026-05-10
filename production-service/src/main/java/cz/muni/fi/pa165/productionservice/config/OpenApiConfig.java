package cz.muni.fi.pa165.productionservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productionServiceOpenApi(
        @Value("${server.servlet.context-path:}") String contextPath
    ) {
        return new OpenAPI()
            .info(new Info()
                .title("Production Service API")
                .version("1.0.0")
                .description("API for managing wine recipes, production planning, quality evaluation, and production batches."))
            .addServersItem(new Server()
                .url(normalizeContextPath(contextPath))
                .description("Production service API context path"));
    }

    private String normalizeContextPath(String contextPath) {
        return contextPath == null || contextPath.isBlank() ? "/" : contextPath;
    }
}
