package br.com.tce.carsystem.infra.security;

import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.domain.user.UserRole;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    private TokenService tokenService;


    public TokenServiceTest() {
        tokenService = new TokenService();
    }

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", "my-secret-key");
    }


    @Test
    public void testGenerateTokenWhenValidUserThenReturnToken() {
        // Arrange
        User user = new User("testLogin", "testPassword", UserRole.USER, "testFirstName", "testLastName", "testEmail", null, "testPhone", Collections.emptyList());

        // Act
        String token = tokenService.generateToken(user);
        // Assert
        assertThat(token).isNotNull();
    }


    @Test
    public void testGenerateTokenWhenJWTCreationExceptionThenThrowRuntimeException() {
        // Arrange
        User user = Mockito.mock(User.class);
        when(user.getLogin()).thenThrow(JWTCreationException.class);

        // Act & Assert
        assertThatThrownBy(() -> tokenService.generateToken(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao gerar token JWT");
    }

    @Test
    public void testValidateTokenWhenTokenIsValidThenReturnSubject() {
        // Arrange
        User user = new User("testLogin", "testPassword", UserRole.USER, "testFirstName", "testLastName", "testEmail", null, "testPhone", Collections.emptyList());

        // Act
        String token = tokenService.generateToken(user);

        String login = tokenService.validateToken(token);

        // Assert
        assertThat(login).isEqualTo(user.getLogin());
    }

    @Test
    public void testValidateTokenWhenTokenIsInvalidThenReturnEmptyString() {
        // Arrange
        String token = "invalidToken";

        // Act
        String subject = tokenService.validateToken(token);

        // Assert
        assertThat(subject).isEmpty();
    }

    @Test
    public void testValidateTokenWhenTokenIsNullThenReturnEmptyString() {
        // Arrange
        String token = null;

        // Act
        String subject = tokenService.validateToken(token);

        // Assert
        assertThat(subject).isEmpty();
    }


}
