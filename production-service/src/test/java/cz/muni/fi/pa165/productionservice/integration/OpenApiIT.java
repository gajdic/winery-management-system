package cz.muni.fi.pa165.productionservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void openApiDocs_defaultPath_containsProductionServiceApi() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.info.title").value("Production Service API"))
            .andExpect(jsonPath("$.paths['/production/recipes']").exists())
            .andExpect(jsonPath("$.paths['/production/planning']").exists())
            .andExpect(jsonPath("$.paths['/production/batches']").exists())
            .andExpect(jsonPath("$.components.securitySchemes.MUNI.type").value("oauth2"))
            .andExpect(jsonPath("$.paths['/production/recipes'].get.security[0].MUNI[0]").value("test_read"))
            .andExpect(jsonPath("$.paths['/production/recipes'].post.security[0].MUNI[0]").value("test_write"))
            .andExpect(jsonPath("$.paths['/production/planning'].post.security[0].MUNI[0]").value("test_read"))
            .andExpect(jsonPath("$.paths['/production/recipes'].get.responses['403']").exists())
            .andExpect(jsonPath("$.paths['/production/recipes'].post.responses['403']").exists())
            .andExpect(jsonPath("$.paths['/production/planning'].post.responses['403']").exists());
    }
}
