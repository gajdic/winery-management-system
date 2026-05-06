package cz.muni.fi.pa165.customerservice.config;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCustomers_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/customers"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getPurchases_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/purchases"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getReviews_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/reviews"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getCustomers_withWrongScope_returns403() throws Exception {
        mockMvc.perform(get("/customers")
                .with(jwt().jwt(token -> token.claim("scope", "write"))))
            .andExpect(status().isForbidden());
    }

    @Test
    void getCustomers_withRightScope_returns200() throws Exception {
        mockMvc.perform(get("/customers")
                .with(jwt().jwt(token -> token.claim("scope", "read"))))
            .andExpect(status().isOk());
    }

    @Test
    void createCustomer_withWrongScope_returns403() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .with(jwt().jwt(token -> token.claim("scope", "read"))))
            .andExpect(status().isForbidden());
    }

    @Test
    void createCustomer_withRightScopeBadRequest_returns400() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .with(jwt().jwt(token -> token.claim("scope", "write"))))
            .andExpect(status().isBadRequest());
    }
}
