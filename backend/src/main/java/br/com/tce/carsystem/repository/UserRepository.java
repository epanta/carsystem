package br.com.tce.carsystem.repository;

import br.com.tce.carsystem.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByLogin(String login);

    User findByEmail(String email);

    Optional<User> findFirstByLastLoginIsNotNullOrderByLastLogin();
}
