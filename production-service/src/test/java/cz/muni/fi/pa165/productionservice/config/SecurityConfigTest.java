package cz.muni.fi.pa165.productionservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.commonservice.config.SecurityScopes;
import cz.muni.fi.pa165.commonservice.config.TestJacksonConfig;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanRequestDto;
import cz.muni.fi.pa165.productionservice.api.WineRecipeCreateDto;
import cz.muni.fi.pa165.productionservice.controller.ProductionPlanningController;
import cz.muni.fi.pa165.productionservice.controller.WineRecipeController;
import cz.muni.fi.pa165.productionservice.facade.ProductionPlanningFacade;
import cz.muni.fi.pa165.productionservice.facade.WineRecipeFacade;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
    WineRecipeController.class,
    ProductionPlanningController.class
})
@AutoConfigureMockMvc
@Import({
    SecurityConfig.class,
    TestJacksonConfig.class,
    WineRecipeController.class,
    ProductionPlanningController.class
})
class SecurityConfigTest {

    @SpringBootConfiguration
    static class TestApplication {
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WineRecipeFacade wineRecipeFacade;

    @MockitoBean
    private ProductionPlanningFacade productionPlanningFacade;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void getEndpoint_withoutToken_returnsUnauthorized() throws Exception {
        mockMvc.perform(get(SecurityConfig.RECIPES_ENDPOINTS.replace("/**", "")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getEndpoint_withWriteScopeOnly_returnsForbidden() throws Exception {
        mockMvc.perform(get(SecurityConfig.RECIPES_ENDPOINTS.replace("/**", ""))
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.WRITE_SCOPE))))
            .andExpect(status().isForbidden());
    }

    @Test
    void getEndpoint_withReadScope_returnsOk() throws Exception {
        Mockito.when(wineRecipeFacade.findAllRecipes()).thenReturn(List.of(TestDataFactory.wineRecipeDto()));

        mockMvc.perform(get(SecurityConfig.RECIPES_ENDPOINTS.replace("/**", ""))
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.READ_SCOPE))))
            .andExpect(status().isOk());
    }

    @Test
    void postRecipe_withReadScopeOnly_returnsForbidden() throws Exception {
        WineRecipeCreateDto createDto = TestDataFactory.wineRecipeCreateDto();

        mockMvc.perform(post(SecurityConfig.RECIPES_ENDPOINTS.replace("/**", ""))
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.READ_SCOPE)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isForbidden());
    }

    @Test
    void postRecipe_withWriteScope_returnsCreated() throws Exception {
        WineRecipeCreateDto createDto = TestDataFactory.wineRecipeCreateDto();
        Mockito.when(wineRecipeFacade.createRecipe(Mockito.any(WineRecipeCreateDto.class)))
            .thenReturn(TestDataFactory.wineRecipeDto());

        mockMvc.perform(post(SecurityConfig.RECIPES_ENDPOINTS.replace("/**", ""))
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.WRITE_SCOPE)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated());
    }

    @Test
    void postPlanning_withReadScope_returnsOk() throws Exception {
        ProductionPlanRequestDto requestDto = TestDataFactory.productionPlanRequestDto();
        requestDto.getAvailableHarvests().get(0).setHarvestDate(LocalDate.now());
        Mockito.when(productionPlanningFacade.planProduction(Mockito.any(ProductionPlanRequestDto.class)))
            .thenReturn(TestDataFactory.productionPlanDto());

        mockMvc.perform(post(SecurityConfig.PLANNING_ENDPOINT)
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.READ_SCOPE)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk());
    }
}
