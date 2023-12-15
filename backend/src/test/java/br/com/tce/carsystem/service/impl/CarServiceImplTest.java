package br.com.tce.carsystem.service.impl;

import br.com.tce.carsystem.domain.car.Car;
import br.com.tce.carsystem.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CarServiceImplTest {

    private CarRepository carRepository;

    private CarServiceImpl carService;

    private Pageable pageable;
    private List<Car> carList;
    private Page<Car> carPage;

    public CarServiceImplTest() {
        carRepository = Mockito.mock(CarRepository.class);
        carService = new CarServiceImpl(carRepository);
    }

    @BeforeEach
    public void setUp() {
        pageable = Pageable.unpaged();
        carList = Collections.singletonList(new Car());
        carPage = new PageImpl<>(carList);
    }

    @Test
    public void testDeleteWhenCarIsDeletedThenCarIsDeleted() {
        Car car = new Car();
        doNothing().when(carRepository).delete(car);

        carService.delete(car);

        verify(carRepository, times(1)).delete(car);
    }

    @Test
    public void testDeleteWhenExceptionThrownThenThrowException() {
        Car car = new Car();
        doThrow(new RuntimeException()).when(carRepository).delete(car);

        assertThrows(RuntimeException.class, () -> carService.delete(car));
    }

    @Test
    public void testFindAllWhenCarsExistThenReturnPageOfCars() {
        when(carRepository.findAll(pageable)).thenReturn(carPage);

        Page<Car> result = carService.findAll(pageable);

        assertEquals(carPage, result);
    }

    @Test
    public void testFindAllWhenNoCarsThenReturnEmptyPage() {
        when(carRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<Car> result = carService.findAll(pageable);

        assertEquals(Page.empty(), result);
    }

    @Test
    public void testFindByLicensePlateWhenCarExistsThenReturnCar() {
        String licensePlate = "ABC-1234";
        Car car = new Car();
        car.setLicensePlate(licensePlate);
        when(carRepository.findByLicensePlate(licensePlate)).thenReturn(Optional.of(car));

        Optional<Car> result = carService.findByLicensePlate(licensePlate);

        assertTrue(result.isPresent());
        assertEquals(car, result.get());
    }

    @Test
    public void testFindByLicensePlateWhenCarDoesNotExistThenReturnEmpty() {
        String licensePlate = "ABC-1234";
        when(carRepository.findByLicensePlate(licensePlate)).thenReturn(Optional.empty());

        Optional<Car> result = carService.findByLicensePlate(licensePlate);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSaveWhenCarIsSavedThenReturnSavedCar() {
        Car car = new Car();
        when(carRepository.save(car)).thenReturn(car);

        Car result = carService.save(car);

        assertEquals(car, result);
    }

    @Test
    public void testSaveWhenCarIsNotSavedThenReturnNull() {
        Car car = new Car();
        when(carRepository.save(car)).thenReturn(null);

        Car result = carService.save(car);

        assertNull(result);
    }

    @Test
    public void testFindByIdWhenCarExistsThenReturnCar() {
        String id = "123";
        Car car = new Car();
        car.setId(id);
        when(carRepository.findById(id)).thenReturn(Optional.of(car));

        Optional<Car> result = carService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(car, result.get());
    }

    @Test
    public void testFindByIdWhenCarDoesNotExistThenReturnEmpty() {
        String id = "123";
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Car> result = carService.findById(id);

        assertTrue(result.isEmpty());
    }
}