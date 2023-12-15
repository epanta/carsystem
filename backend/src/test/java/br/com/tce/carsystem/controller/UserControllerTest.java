package br.com.tce.carsystem.controller;

import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.domain.user.UserRequestDTO;
import br.com.tce.carsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testUploadFileWhenUserExistsAndImageIsValidThenReturnOk() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setLogin("test");
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");
        userRequestDTO.setImage("testImage");

        User user = new User();
        user.setLogin("test");

        Mockito.when(userService.findById("1")).thenReturn(Optional.of(user));
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("test"));
    }

    @Test
    public void testUploadFileWhenUserDoesNotExistThenReturnNotFound() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setLogin("test");
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");
        userRequestDTO.setImage("testImage");
        userRequestDTO.setImage("testImage");

        Mockito.when(userService.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car not found"));
    }

    @Test
    public void testUploadFileWhenImageIsInvalidThenReturnExpectationFailed() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setLogin("test");
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");
        userRequestDTO.setImage("testImage");
        userRequestDTO.setImage("");

        User user = new User();
        user.setLogin("test");

        Mockito.when(userService.findById("1")).thenReturn(Optional.of(user));
        Mockito.when(userService.save(Mockito.any(User.class))).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not upload the file"));
    }

    @Test
    @WithMockUser
    public void testUpdateWhenUserIsUpdatedThenReturnUser() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setLogin("test");
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");

        User user = new User();
        user.setLogin("test");

        Mockito.when(userService.findById("1")).thenReturn(Optional.of(user));
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("test"));
    }

    @Test
    public void testUpdateWhenUserNotFoundThenReturnError() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setLogin("test");
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");

        Mockito.when(userService.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));
    }

    @Test
    public void testUpdateWhenInvalidUserRequestThenReturnError() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid fields"));
    }

    @Test
    public void testDeleteWhenUserExistsThenReturnOk() throws Exception {
        User user = new User();
        user.setLogin("test");

        Mockito.when(userService.findById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User removed successfully"));

        Mockito.verify(userService, Mockito.times(1)).delete(user);
    }

    @Test
    public void testDeleteWhenUserDoesNotExistThenReturnNotFound() throws Exception {
        Mockito.when(userService.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));

        Mockito.verify(userService, Mockito.never()).delete(Mockito.any(User.class));
    }

    @Test
    public void testFindWhenUserExistsThenReturnUserResponseDTO() throws Exception {
        User user = new User();
        user.setLogin("test");

        Mockito.when(userService.findById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("test"));
    }

    @Test
    public void testFindWhenUserDoesNotExistThenReturnErrorResponse() throws Exception {
        Mockito.when(userService.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));
    }

    @Test
    public void testFindWhenServiceThrowsExceptionThenReturnInternalServerError() throws Exception {
        Mockito.when(userService.findById("1")).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void testFindAllWhenValidParametersThenReturnPageOfUserResponseDTO() throws Exception {
        User user = new User();
        user.setLogin("test");
        Page<User> page = new PageImpl<>(Collections.singletonList(user));

        Mockito.when(userService.findAll(PageRequest.of(0, 1))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?page=0&count=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].login").value("test"));
    }

    @Test
    public void testFindAllWhenServiceThrowsExceptionThenReturnInternalServerError() throws Exception {
        Mockito.when(userService.findAll(PageRequest.of(0, 1))).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?page=0&count=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void testRegisterWhenValidUserThenReturnUserResponseDTO() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setLogin("test");
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");

        User user = new User();
        user.setLogin("test");

        Mockito.when(userService.exitsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        Mockito.when(userService.existsByLogin(userRequestDTO.getLogin())).thenReturn(false);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value("test"));
    }

    @Test
    public void testRegisterWhenEmailExistsThenReturnErrorResponse() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setLogin("test");
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");

        Mockito.when(userService.exitsByEmail(userRequestDTO.getEmail())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email already exists"));
    }

    @Test
    public void testRegisterWhenLoginExistsThenReturnErrorResponse() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setLogin("test");
        userRequestDTO.setFirstName("TestUser");
        userRequestDTO.setLastName("Mock");
        userRequestDTO.setEmail("test.user@mock.com.br");
        userRequestDTO.setPhone("988888889");
        userRequestDTO.setPassword("482C811DA5D5B4BC6D497FFA98491E38");
        userRequestDTO.setRole("USER");

        Mockito.when(userService.existsByLogin(userRequestDTO.getLogin())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login already exists"));
    }

}