package br.com.tce.carsystem.service;

import br.com.tce.carsystem.domain.car.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CarService {
    Page<Car> findAll(Pageable pageable);

    Optional<Car> findById(String id);

    void delete(Car car);

    Car save(Car car);

    Boolean existsByLicensePlate(String login);

    Optional<Car> findByLicensePlate(String licensePlate);

    Integer increaseCounter(Integer counter);
}
