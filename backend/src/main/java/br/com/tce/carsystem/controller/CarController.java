package br.com.tce.carsystem.controller;

import br.com.tce.carsystem.domain.car.Car;
import br.com.tce.carsystem.domain.car.CarDTO;
import br.com.tce.carsystem.infra.security.TokenService;
import br.com.tce.carsystem.response.ResponseError;
import br.com.tce.carsystem.response.ResponseMessage;
import br.com.tce.carsystem.service.AuthorizationService;
import br.com.tce.carsystem.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class CarController {

    private final CarService carService;

    private final TokenService tokenService;

    private final AuthorizationService authorizationService;

    @GetMapping
    ResponseEntity<Page<CarDTO>> findAll(@RequestParam(value = "page") Integer page,
                                         @RequestParam(value = "count") Integer count) {
        final var pageable = PageRequest.of(page, count);
        final var carPage = carService.findAll(pageable);
        final var carDTOPage = toPageCarDto(carPage);
        return ResponseEntity.status(HttpStatus.OK).body(carDTOPage);
    }

    @PostMapping
    ResponseEntity<?> register(@RequestHeader("Authorization") String token,
                               @RequestBody @Valid final CarDTO carDTO) {
        if (carService.existsByLicensePlate(carDTO.getLicensePlate())){
            return ResponseEntity.badRequest().body(ResponseError.builder()
                    .message("License plate already exists").errorCode(HttpStatus.BAD_REQUEST.value()).build());
        }

        final var login = tokenService.getLoginFromToken(token);
        final var user = authorizationService.loadUserByUsername(login);

        final var car = new ModelMapper().map(carDTO, Car.class);
        car.setUserId(user.getId());

        final var savedCar = carService.save(car);
        final var carDtoResult = new ModelMapper().map(savedCar, CarDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(carDtoResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable(value = "id") final String id){
        Optional<Car> optionalCar = carService.findById(id);
        if(!optionalCar.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.builder()
                    .message("Car not found").errorCode(HttpStatus.NOT_FOUND.value()).build());
        }

        final var carResponse = new ModelMapper().map(optionalCar.get(), CarDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(carResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") final String id){
        Optional<Car> optionalCar = carService.findById(id);
        if (!optionalCar.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.builder()
                    .message("Car not found").errorCode(HttpStatus.NOT_FOUND.value()).build());
        }
        carService.delete(optionalCar.get());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.builder()
                .message("Car removed successfully.")
                .statusCode(HttpStatus.OK.value()).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") final String id,
                                    @RequestBody @Valid final CarDTO carDTO){
        final var optionalCar = carService.findById(id);
        if (!optionalCar.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.builder()
                    .message("Car not found").errorCode(HttpStatus.NOT_FOUND.value()).build());
        }

        final var currentCar = optionalCar.get();

        final var car = new ModelMapper().map(carDTO, Car.class);
        car.setId(currentCar.getId());

        final var newCounter = carService.increaseCounter(currentCar.getCounter());
        car.setCounter(newCounter);

        carService.save(car);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.builder()
                .message("Car updated successfully.")
                .statusCode(HttpStatus.OK.value()).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> uploadFile(@PathVariable final String id,
                                        @RequestBody @Valid final CarDTO carDTO) {
        try {
            final var optionalCar = this.carService.findById(id);

            if (!optionalCar.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.builder()
                        .message("Car not found").errorCode(HttpStatus.NOT_FOUND.value()).build());
            }

            final var car = optionalCar.get();

            car.setImage(carDTO.getImage().getBytes(StandardCharsets.UTF_8));

            final var carResponse = carService.save(car);

            final var carDtoResult = new ModelMapper().map(carResponse, CarDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body(carDtoResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseError.builder()
                    .message("Could not upload the file").errorCode(HttpStatus.EXPECTATION_FAILED.value()).build());
        }
    }

    private Page<CarDTO> toPageCarDto(Page<Car> objects) {
        return objects.map(this::convertToCarDto);
    }

    private CarDTO convertToCarDto(final Car car) {
        return new ModelMapper().map(car, CarDTO.class);
    }
}
