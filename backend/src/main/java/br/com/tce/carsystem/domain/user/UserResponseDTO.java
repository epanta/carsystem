package br.com.tce.carsystem.domain.user;

import br.com.tce.carsystem.domain.car.CarDTO;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private Date birthday;

    private String login;

    private String role;

    private String phone;

    private Date createdAt;

    private Date lastLogin;

    private String image;

    private List<CarDTO> cars;
}
