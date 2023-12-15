package br.com.tce.carsystem.repository;

import br.com.tce.carsystem.domain.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {

    Boolean existsByLicensePlate(String licensePlate);

    Optional<Car> findByLicensePlate(String licensePlate);
}
