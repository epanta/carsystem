package br.com.tce.carsystem.infra.security;

import br.com.tce.carsystem.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class TokenService {

    @Value("${api.security.token.security}")
    private String secret;

    public String generateToken(User user) {
        try {
            final var algorithm = Algorithm.HMAC256(secret);
            final var token = JWT.create()
                    .withIssuer("car-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(genExpirationTime())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String validateToken(String token) {
        try {
            final var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("car-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            log.warn("Error validating toke -> {}", e.getMessage());
        }

        return "";
    }

    public String getLoginFromToken(final String token) {
        final var formattedToken = token.replace("Bearer ", "");
        return validateToken(formattedToken);
    }


    private Instant genExpirationTime() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
