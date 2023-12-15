package br.com.tce.carsystem.service;

import br.com.tce.carsystem.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> findAll(Pageable pageable);

    Boolean existsByLogin(String login);

    User save(User user);

    Optional<User> findById(String id);

    void delete(User user);

    Boolean exitsByEmail(String email);

    void updateLastLogin(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdLastLogin();
}
