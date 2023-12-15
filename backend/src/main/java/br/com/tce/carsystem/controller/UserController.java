package br.com.tce.carsystem.controller;

import br.com.tce.carsystem.domain.car.CarDTO;
import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.domain.user.UserRequestDTO;
import br.com.tce.carsystem.domain.user.UserResponseDTO;
import br.com.tce.carsystem.exception.InternalErrorException;
import br.com.tce.carsystem.response.ResponseError;
import br.com.tce.carsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<Page<UserResponseDTO>> findAll(@RequestParam(value = "page") Integer page,
                                                  @RequestParam(value = "count") Integer count) throws InternalErrorException {
        final var pageable = PageRequest.of(page, count);
        try {
            final var user = userService.findAll(pageable);
            final var userDto = toPageUserDto(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (Exception e) {
            throw new InternalErrorException("Internal Server Error");
        }

    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO data) {
        if (userService.exitsByEmail(data.getEmail())) {
            return ResponseEntity.badRequest().body(ResponseError.builder()
                    .message("Email already exists").errorCode(HttpStatus.BAD_REQUEST.value()).build());
        }

        if (userService.existsByLogin(data.getLogin())) {
            return ResponseEntity.badRequest().body(ResponseError.builder()
                    .message("Login already exists").errorCode(HttpStatus.BAD_REQUEST.value()).build());
        }

        final var encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        final var newUser = new ModelMapper().map(data, User.class);

        newUser.setPassword(encryptedPassword);

        final var userResponse = userService.save(newUser);
        log.info("M::register, user {} registered successfully", userResponse.getLogin());
        final var userResponseDTO = new ModelMapper().map(userResponse, UserResponseDTO.class);

        return ResponseEntity.ok().body(userResponseDTO);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable(value = "id") final String id) throws InternalErrorException {
        try {
            Optional<User> optionalUser = userService.findById(id);
            if (!optionalUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.builder()
                        .message("User not found").errorCode(HttpStatus.NOT_FOUND.value()).build());
            }
            final var userResponseDTO = new ModelMapper().map(optionalUser.get(), UserResponseDTO.class);

            if (userResponseDTO.getCars() != null && !userResponseDTO.getCars().isEmpty()) {
                userResponseDTO.setCars(userResponseDTO.getCars().stream().sorted(Comparator.comparing(CarDTO::getCounter).reversed()
                        .thenComparing(CarDTO::getModel).thenComparing(CarDTO::getModel)).collect(Collectors.toList()));
            }

            return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
        } catch (Exception e) {
            throw new InternalErrorException("Internal Server Error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") final String id){
        Optional<User> optionalUser = userService.findById(id);
        if (!optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.builder()
                    .message("User not found").errorCode(HttpStatus.NOT_FOUND.value()).build());
        }
        userService.delete(optionalUser.get());
        return ResponseEntity.status(HttpStatus.OK).body("User removed successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") String id,
                                         @RequestBody @Valid UserRequestDTO userRequestDto){
        final var optionalUser = userService.findById(id);
        if (!optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.builder()
                    .message("User not found").errorCode(HttpStatus.NOT_FOUND.value()).build());
        }

        final var user = new ModelMapper().map(userRequestDto, User.class);
        user.setId(optionalUser.get().getId());

        final var userResponse = userService.save(user);
        final var userResponseDto = new ModelMapper().map(userResponse, UserResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> uploadFile(@PathVariable final String id,
                                        @RequestBody @Valid final UserRequestDTO UserRequestDTO) {
        try {
            final var optionalUser = this.userService.findById(id);

            if (!optionalUser.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError.builder()
                        .message("Car not found").errorCode(HttpStatus.NOT_FOUND.value()).build());
            }

            final var user = optionalUser.get();

            user.setImage(UserRequestDTO.getImage().getBytes(StandardCharsets.UTF_8));

            final var userResponse = userService.save(user);

            final var userDtoResult = new ModelMapper().map(userResponse, UserResponseDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body(userDtoResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseError.builder()
                    .message("Could not upload the file").errorCode(HttpStatus.EXPECTATION_FAILED.value()).build());
        }
    }

    private Page<UserResponseDTO> toPageUserDto(Page<User> users) {
        return users.map(this::convertToUserDto);
    }

    private UserResponseDTO convertToUserDto(User user) {
        final var userResponseDTO = new ModelMapper().map(user, UserResponseDTO.class);

        if (userResponseDTO.getCars() != null && !userResponseDTO.getCars().isEmpty()) {
            userResponseDTO.setCars(userResponseDTO.getCars().stream().sorted(Comparator.comparing(CarDTO::getCounter).reversed()
                    .thenComparing(CarDTO::getModel).thenComparing(CarDTO::getModel)).collect(Collectors.toList()));
        }

        return userResponseDTO;
    }
}
