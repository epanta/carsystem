package br.com.tce.carsystem.domain.info;

import br.com.tce.carsystem.domain.car.CarDTO;
import lombok.*;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponseDTO {

    private String firstName;

    private String lastName;

    private String email;

    private Date birthday;

    private String login;

    private String phone;

    private Date createdAt;

    private Date lastLogin;

    private CarDTO cars;
}
