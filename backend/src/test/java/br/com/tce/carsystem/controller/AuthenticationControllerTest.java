package br.com.tce.carsystem.controller;

import br.com.tce.carsystem.domain.auth.AuthenticationDTO;
import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.domain.user.UserRole;
import br.com.tce.carsystem.infra.security.TokenService;
import br.com.tce.carsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;

    @Test
    public void testLoginWhenValidCredentialsThenReturnTokenAndUserInfo() throws Exception {
        User user = new User("test", "test", UserRole.USER, "Test", "User", "test@test.com", new Date(), "1234567890", new ArrayList<>());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(tokenService.generateToken(any())).thenReturn("token");

        mockMvc.perform(post("/api/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new AuthenticationDTO("test", "test"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("token")))
                .andExpect(jsonPath("$.login", is("test")));
    }

    @Test
    public void testLoginWhenInvalidCredentialsThenReturnUnauthorized() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new AuthenticationDTO("test", "test"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoginWhenUserNotFoundThenReturnUnauthorized() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(post("/api/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new AuthenticationDTO("test", "test"))))
                .andExpect(status().isUnauthorized());
    }
}