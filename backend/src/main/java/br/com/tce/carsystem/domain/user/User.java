package br.com.tce.carsystem.domain.user;

import br.com.tce.carsystem.domain.car.Car;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "users")
@Entity(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String login;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private String phone;
    private Date createdAt;
    @Lob
    private byte[] image;
    private Date lastLogin;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private List<Car> cars;

    public User(String login, String password, UserRole role, String firstName, String lastName, String email, Date birthday, String phone, List<Car> cars){
        this.login = login;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.phone = phone;
        this.cars = cars;
    }

    public String getImage() {
        if (image != null) {
            return new String(image, StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}
