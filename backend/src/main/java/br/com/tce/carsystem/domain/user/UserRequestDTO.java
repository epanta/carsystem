package br.com.tce.carsystem.domain.user;

import br.com.tce.carsystem.domain.car.CarDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {

    @NotBlank(message = "Missing fields")
    private String firstName;

    @NotBlank(message = "Missing fields")
    private String lastName;

    @NotBlank(message = "Missing fields")
    @Email(message = "Invalid fields")
    private String email;

    private Date birthday;

    @NotBlank(message = "Missing fields")
    private String login;

    @NotBlank(message = "Missing fields")
    private String password;

    @NotBlank(message = "Missing fields")
    private String role;

    @NotBlank(message = "Missing fields")
    @Length(min = 9, message = "Invalid fields")
    private String phone;

    private String image;

    @Valid
    private List<CarDTO> cars;

}
