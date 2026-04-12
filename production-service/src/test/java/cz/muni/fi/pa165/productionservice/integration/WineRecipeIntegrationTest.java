package cz.muni.fi.pa165.productionservice.integration;

import com.jayway.jsonpath.JsonPath;
import cz.muni.fi.pa165.productionservice.data.repository.WineRecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
class WineRecipeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WineRecipeRepository wineRecipeRepository;

    @Test
    void createAndFetchRecipe_validRequest_persistsRecipe() throws Exception {
        String request = """
            {
              "name": "Integration Riesling",
              "productionYear": 2026,
              "alcoholPercentage": 12.5,
              "tastingNotes": "Fresh citrus notes",
              "bottleVolumeLiters": 0.75,
              "grapeComponents": [
                {
                  "grapeType": "Riesling",
                  "kilogramsPerLiter": 1.3,
                  "preferredQuality": 85
                }
              ],
              "additionalIngredients": [
                {
                  "name": "Yeast",
                  "quantityPerLiter": 0.02,
                  "unit": "kg"
                }
              ]
            }
            """;

        String response = mockMvc.perform(post("/production/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Integration Riesling"))
            .andExpect(jsonPath("$.grapeComponents[0].grapeType").value("Riesling"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        UUID recipeId = UUID.fromString(JsonPath.read(response, "$.id"));

        mockMvc.perform(get("/production/recipes/{id}", recipeId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(recipeId.toString()))
            .andExpect(jsonPath("$.name").value("Integration Riesling"))
            .andExpect(jsonPath("$.additionalIngredients[0].name").value("Yeast"));

        assertThat(wineRecipeRepository.findById(recipeId)).isPresent();
    }
}
