package br.com.tce.carsystem.controller;

import br.com.tce.carsystem.domain.car.Car;
import br.com.tce.carsystem.domain.car.CarDTO;
import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.service.AuthorizationService;
import br.com.tce.carsystem.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    @MockBean
    private CarService carService;

    @MockBean
    private AuthorizationService authorizationService;

    @InjectMocks
    private CarController carController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Existing tests...

    @Test
    @WithMockUser
    public void testRegisterWhenValidCarThenReturnCreated() throws Exception {
        // Arrange
        CarDTO carDTO = new CarDTO();
        carDTO.setYear(2021);
        carDTO.setLicensePlate("ABC-1234");
        carDTO.setModel("Model");
        carDTO.setColor("Color");
        carDTO.setCounter(1);

        User user = new User();
        user.setId("1");

        Car car = new Car();
        car.setId("1");
        car.setYear(2021);
        car.setLicensePlate("ABC-1234");
        car.setModel("Model");
        car.setColor("Color");
        car.setCounter(1);
        car.setUserId("1");

        when(carService.existsByLicensePlate("ABC-1234")).thenReturn(false);
        when(authorizationService.loadUserByUsername(any())).thenReturn(user);
        when(carService.save(any(Car.class))).thenReturn(car);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2021))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate").value("ABC-1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("Color"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.counter").value(1));
    }

    @Test
    @WithMockUser
    public void testRegisterWhenExistingLicensePlateThenReturnBadRequest() throws Exception {
        // Arrange
        CarDTO carDTO = new CarDTO();
        carDTO.setYear(2021);
        carDTO.setLicensePlate("ABC-1234");
        carDTO.setModel("Model");
        carDTO.setColor("Color");
        carDTO.setCounter(1);

        when(carService.existsByLicensePlate("ABC-1234")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("License plate already exists"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(400));
    }

    @Test
    @WithMockUser
    public void testDeleteWhenCarExistsThenReturnSuccess() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId("1");
        when(carService.findById("1")).thenReturn(Optional.of(car));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car removed successfully."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200));
    }

    @Test
    @WithMockUser
    public void testDeleteWhenCarDoesNotExistThenReturnNotFound() throws Exception {
        // Arrange
        when(carService.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(404));
    }

    @Test
    @WithMockUser
    public void testUpdateWhenCarIsUpdatedThenReturnOk() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId("1");
        car.setYear(2020);
        car.setLicensePlate("ABC-1234");
        car.setModel("Model");
        car.setColor("Color");
        car.setCounter(1);
        when(carService.findById("1")).thenReturn(Optional.of(car));
        when(carService.save(any(Car.class))).thenReturn(car);
        when(carService.increaseCounter(1)).thenReturn(2);

        CarDTO carDTO = new CarDTO();
        carDTO.setId("1");
        carDTO.setYear(2021);
        carDTO.setLicensePlate("ABC-1234");
        carDTO.setModel("New Model");
        carDTO.setColor("New Color");
        carDTO.setCounter(2);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car updated successfully."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200));
    }

    @Test
    @WithMockUser
    public void testUpdateWhenCarIsNotFoundThenReturnNotFound() throws Exception {
        // Arrange
        when(carService.findById("1")).thenReturn(Optional.empty());

        CarDTO carDTO = new CarDTO();
        carDTO.setId("1");
        carDTO.setYear(2021);
        carDTO.setLicensePlate("ABC-1234");
        carDTO.setModel("New Model");
        carDTO.setColor("New Color");
        carDTO.setCounter(2);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(404));
    }

    @Test
    @WithMockUser
    public void testFindAllWhenCarsExistAndValidParametersThenReturnCarDTOPage() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId("1");
        car.setYear(2020);
        car.setLicensePlate("ABC-1234");
        car.setModel("Model");
        car.setColor("Color");
        car.setCounter(1);
        List<Car> cars = Arrays.asList(car);
        Page<Car> carPage = new PageImpl<>(cars);
        when(carService.findAll(PageRequest.of(0, 5))).thenReturn(carPage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars?page=0&count=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].year").value(2020))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].licensePlate").value("ABC-1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].model").value("Model"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].color").value("Color"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].counter").value(1));
    }

    @Test
    @WithMockUser
    public void testFindAllWhenNegativeParametersThenReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars?page=-1&count=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testFindAllWhenInvalidParametersThenReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars?page=a&count=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testFindAllWhenNoCarsThenReturnEmptyPage() throws Exception {
        // Arrange
        Page<Car> carPage = Page.empty();
        when(carService.findAll(PageRequest.of(0, 5))).thenReturn(carPage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars?page=0&count=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty());
    }

    @Test
    @WithMockUser
    public void testUpdateWhenCarFoundThenReturnSuccess() throws Exception {
        // Arrange
        Car car = new Car();
        car.setId("1");
        car.setYear(2020);
        car.setLicensePlate("ABC-1234");
        car.setModel("Model");
        car.setColor("Color");
        car.setCounter(1);
        when(carService.findById("1")).thenReturn(Optional.of(car));
        when(carService.save(any(Car.class))).thenReturn(car);
        when(carService.increaseCounter(1)).thenReturn(2);

        CarDTO carDTO = new CarDTO();
        carDTO.setId("1");
        carDTO.setYear(2021);
        carDTO.setLicensePlate("ABC-1234");
        carDTO.setModel("New Model");
        carDTO.setColor("New Color");
        carDTO.setCounter(2);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car updated successfully."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200));
    }

    @Test
    @WithMockUser
    public void testUpdateWhenCarNotFoundThenReturnError() throws Exception {
        // Arrange
        when(carService.findById("1")).thenReturn(Optional.empty());

        CarDTO carDTO = new CarDTO();
        carDTO.setId("1");
        carDTO.setYear(2021);
        carDTO.setLicensePlate("ABC-1234");
        carDTO.setModel("New Model");
        carDTO.setColor("New Color");
        carDTO.setCounter(2);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(404));
    }

    @Test
    @WithMockUser
    public void testUpdateWhenInvalidCarDTOThenReturnError() throws Exception {
        // Arrange
        CarDTO carDTO = new CarDTO();
        carDTO.setId("1");
        carDTO.setYear(1799); // Invalid year
        carDTO.setLicensePlate("ABC-1234");
        carDTO.setModel("New Model");
        carDTO.setColor("New Color");
        carDTO.setCounter(2);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isBadRequest());
    }

    // Existing tests...
}