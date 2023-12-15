package br.com.tce.carsystem.service.impl;

import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceImplTest {


    private UserRepository userRepository;

    private UserServiceImpl userService;

    public UserServiceImplTest() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testFindAllWhenUsersExistThenReturnPageOfUsers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        List<User> userList = Collections.singletonList(user);
        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());
        when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(userPage);

        // Act
        Page<User> result = userService.findAll(pageable);

        // Assert
        assertEquals(userPage, result);
    }

    @Test
    public void testFindAllWhenNoUsersExistThenReturnEmptyPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = Page.empty(pageable);
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // Act
        Page<User> result = userService.findAll(pageable);

        // Assert
        assertEquals(userPage, result);
    }

    @Test
    public void testSaveWhenUserIsSavedThenReturnUser() {
        // Arrange
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.save(user);

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void testSaveWhenUserIsNotSavedThenReturnNull() {
        // Arrange
        User user = new User();
        when(userRepository.save(user)).thenReturn(null);

        // Act
        User result = userService.save(user);

        // Assert
        assertNull(result);
    }

    @Test
    public void testExistsByLoginWhenUserExistsThenReturnTrue() {
        // Arrange
        String login = "testLogin";
        User user = new User();
        user.setLogin(login);
        when(userRepository.findByLogin(login)).thenReturn(user);

        // Act
        Boolean result = userService.existsByLogin(login);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testExistsByLoginWhenUserDoesNotExistThenReturnFalse() {
        // Arrange
        String login = "testLogin";
        when(userRepository.findByLogin(login)).thenReturn(null);

        // Act
        Boolean result = userService.existsByLogin(login);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testFindByIdWhenUserExistsThenReturnOptionalWithUser() {
        // Arrange
        String id = "testId";
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindByIdWhenUserDoesNotExistThenReturnEmptyOptional() {
        // Arrange
        String id = "testId";
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(id);

        // Assert
        assertFalse(result.isPresent());
    }
}