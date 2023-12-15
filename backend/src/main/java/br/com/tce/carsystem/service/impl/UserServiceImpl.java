package br.com.tce.carsystem.service.impl;

import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.repository.UserRepository;
import br.com.tce.carsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Boolean existsByLogin(final String login) {
        final var userLogin = userRepository.findByLogin(login);
        return userLogin != null ? Boolean.TRUE: Boolean.FALSE;
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public Boolean exitsByEmail(final String email) {
        final var userEmail = findByEmail(email);
        return userEmail != null ? Boolean.FALSE: Boolean.TRUE;
    }

    @Override
    public void updateLastLogin(User user) {
        user.setLastLogin(new Date());
        this.save(user);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public Optional<User> findByIdLastLogin() {
        return userRepository.findFirstByLastLoginIsNotNullOrderByLastLogin();
    }
}
