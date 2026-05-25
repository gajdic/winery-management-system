package cz.muni.fi.pa165.inventoryservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryServiceOpenApi(
        @Value("${server.servlet.context-path:}") String contextPath
    ) {
        return new OpenAPI()
            .info(new Info()
                .title("Inventory Service API")
                .version("1.0.0")
                .description("API for managing wine stock quantities and customer reservations of bottled wines."))
            .addServersItem(new Server()
                .url("/inventory-api" + normalizeContextPath(contextPath))
                .description("Inventory service via central Swagger UI proxy"))
            .addServersItem(new Server()
                .url(normalizeContextPath(contextPath))
                .description("Inventory service direct (when accessed on port 8083)"));
    }

    private String normalizeContextPath(String contextPath) {
        return contextPath == null || contextPath.isBlank() ? "" : contextPath;
    }
}
