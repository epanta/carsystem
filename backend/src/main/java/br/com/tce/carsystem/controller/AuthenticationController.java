package br.com.tce.carsystem.controller;

import br.com.tce.carsystem.domain.auth.AuthenticationDTO;
import br.com.tce.carsystem.domain.auth.LoginResponseDTO;
import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.infra.security.TokenService;
import br.com.tce.carsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        final var auth = authenticationManager.authenticate(usernamePassword);
        final var user = (User) auth.getPrincipal();
        final var token = tokenService.generateToken(user);

        userService.updateLastLogin(user);

        return ResponseEntity.ok(new LoginResponseDTO(token, user.getLogin(), user.getId(), user.getRole().getRole()));
    }
}
