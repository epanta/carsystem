package br.com.tce.carsystem.domain.car;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

        String id;
        @Min(value = 1800, message = "Invalid fields")
        @Max(value = 2100, message = "Invalid fields")
        Integer year;
        String licensePlate;
        String model;
        String color;
        Integer counter;
        String image;
}
