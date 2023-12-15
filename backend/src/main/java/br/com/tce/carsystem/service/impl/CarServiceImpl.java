package br.com.tce.carsystem.service.impl;

import br.com.tce.carsystem.domain.car.Car;
import br.com.tce.carsystem.repository.CarRepository;
import br.com.tce.carsystem.service.CarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private final CarRepository repository;
    @Override
    public Page<Car> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Car> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Car car) {
        this.repository.delete(car);
    }

    @Override
    public Car save(Car car) {
        return this.repository.save(car);
    }

    @Override
    public Boolean existsByLicensePlate(final String licensePlate) {
        return this.repository.existsByLicensePlate(licensePlate);
    }

    @Override
    public Optional<Car> findByLicensePlate(final String licensePlate) {
        log.info("M:: findByLicensePlate, searching by license plate.");
        return this.repository.findByLicensePlate(licensePlate);
    }

    @Override
    public Integer increaseCounter(final Integer counter) {
        log.info("M:: increaseCounter, increasing counter. Current counter value -> {}", counter);
        return counter + 1;
    }
}
