package cz.muni.fi.pa165.customerservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerServiceOpenApi(
        @Value("${server.servlet.context-path:}") String contextPath
    ) {
        return new OpenAPI()
            .info(new Info()
                .title("Customer Service API")
                .version("1.0.0")
                .description("API for managing customers, purchase records, and customer reviews of wines."))
            .addServersItem(new Server()
                .url(normalizeContextPath(contextPath))
                .description("Customer service API context path"));
    }

    private String normalizeContextPath(String contextPath) {
        return contextPath == null || contextPath.isBlank() ? "/" : contextPath;
    }
}
