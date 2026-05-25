package cz.muni.fi.pa165.harvestservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI harvestServiceOpenApi(
        @Value("${server.servlet.context-path:}") String contextPath
    ) {
        return new OpenAPI()
            .info(new Info()
                .title("Harvest Service API")
                .version("1.0.0")
                .description("API for managing vineyards, grape varieties, harvest records, and harvest summaries."))
            .addServersItem(new Server()
                .url("/harvest-api" + normalizeContextPath(contextPath))
                .description("Harvest service via central Swagger UI proxy"))
            .addServersItem(new Server()
                .url(normalizeContextPath(contextPath))
                .description("Harvest service direct (when accessed on port 8081)"));
    }

    private String normalizeContextPath(String contextPath) {
        return contextPath == null || contextPath.isBlank() ? "" : contextPath;
    }
}
