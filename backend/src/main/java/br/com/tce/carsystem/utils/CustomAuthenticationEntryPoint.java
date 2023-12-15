package br.com.tce.carsystem.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final var writer = response.getWriter();
        final var header = req.getHeader("Authorization");
        if (header == null || "".equals(header) ) {
            writer.printf("Unauthorized");
        } else {
            writer.printf("Unauthorized - invalid session");
        }
    }
}
