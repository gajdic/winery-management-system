package cz.muni.fi.pa165.harvestservice.config;

import cz.muni.fi.pa165.commonservice.config.SecurityScopes;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardCreateDto;
import cz.muni.fi.pa165.harvestservice.api.dto.VineyardViewDto;
import cz.muni.fi.pa165.harvestservice.facade.VineyardFacade;
import cz.muni.fi.pa165.harvestservice.rest.VineyardRestController;
import cz.muni.fi.pa165.harvestservice.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VineyardRestController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private VineyardFacade vineyardFacade;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void getEndpoint_withoutToken_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/vineyards"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getEndpoint_withWriteScopeOnly_returnsForbidden() throws Exception {
        mockMvc.perform(get("/vineyards")
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.WRITE_SCOPE))))
            .andExpect(status().isForbidden());
    }

    @Test
    void getEndpoint_withReadScope_returnsOk() throws Exception {
        Mockito.when(vineyardFacade.findAll()).thenReturn(List.of(TestDataFactory.vineyardViewDto()));

        mockMvc.perform(get("/vineyards")
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.READ_SCOPE))))
            .andExpect(status().isOk());
    }

    @Test
    void postEndpoint_withReadScopeOnly_returnsForbidden() throws Exception {
        VineyardCreateDto createDto = TestDataFactory.vineyardCreateDto();

        mockMvc.perform(post("/vineyards")
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.READ_SCOPE)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isForbidden());
    }

    @Test
    void postEndpoint_withWriteScope_returnsCreated() throws Exception {
        VineyardCreateDto createDto = TestDataFactory.vineyardCreateDto();
        VineyardViewDto viewDto = TestDataFactory.vineyardViewDto();
        Mockito.when(vineyardFacade.create(Mockito.any(VineyardCreateDto.class))).thenReturn(viewDto);

        mockMvc.perform(post("/vineyards")
                .with(jwt().jwt(token -> token.claim("scope", SecurityScopes.WRITE_SCOPE)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated());
    }
}
