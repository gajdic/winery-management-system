package cz.muni.fi.pa165.productionservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import cz.muni.fi.pa165.commonservice.config.TestJacksonConfig;
import cz.muni.fi.pa165.productionservice.data.repository.WineRecipeRepository;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestJacksonConfig.class)
class WineRecipeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WineRecipeRepository wineRecipeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAndFetchRecipe_validRequest_persistsRecipe() throws Exception {
        String request = objectMapper.writeValueAsString(TestDataFactory.wineRecipeCreateDto());

        String response = mockMvc.perform(post("/production/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Riesling"))
            .andExpect(jsonPath("$.grapeComponents[0].grapeType").value("Riesling"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        UUID recipeId = UUID.fromString(JsonPath.read(response, "$.id"));

        mockMvc.perform(get("/production/recipes/{id}", recipeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(recipeId.toString()))
            .andExpect(jsonPath("$.name").value("Riesling"))
            .andExpect(jsonPath("$.additionalIngredients[0].name").value("Yeast"));

        assertThat(wineRecipeRepository.findById(recipeId)).isPresent();
    }
}
