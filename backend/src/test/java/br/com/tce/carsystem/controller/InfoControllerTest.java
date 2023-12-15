package br.com.tce.carsystem.controller;

import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.service.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @Mock
    private User user;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    @WithMockUser(username = "testUser", authorities = {
            "ROLE_USER" })
    public void testInfoWhenAuthenticatedThenReturn200() throws Exception {

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, Set.of(new SimpleGrantedAuthority("ROLE_USER"))));

        when(user.getUsername()).thenReturn("testUser");
        when(user.getLogin()).thenReturn("testUser");
        when(user.getFirstName()).thenReturn("Test");
        when(user.getLastName()).thenReturn("User");
        when(user.getEmail()).thenReturn("test.user@example.com");
        when(user.getPhone()).thenReturn("1234567890");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/me"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"firstName\":\"Test\",\"lastName\":\"User\",\"email\":\"test.user@example.com\",\"birthday\":null,\"login\":\"testUser\",\"phone\":\"1234567890\",\"createdAt\":null,\"lastLogin\":null,\"cars\":null}"));
    }

    @Test
    public void testInfoWhenNotAuthenticatedThenReturn401() throws Exception {
        SecurityContextHolder.clearContext();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/me"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}