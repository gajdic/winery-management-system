package cz.muni.fi.pa165.productionservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productionServiceOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Production Service API")
                .version("1.0.0")
                .description("API for managing wine recipes, production planning, quality evaluation, and production batches."))
            .addServersItem(new Server()
                .url("/api")
                .description("Production service API context path"));
    }
}
