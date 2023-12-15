package br.com.tce.carsystem.controller;

import br.com.tce.carsystem.domain.info.UserInfoResponseDTO;
import br.com.tce.carsystem.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class InfoController {

    @GetMapping
    ResponseEntity<?> info() {
        final var auth = SecurityContextHolder.getContext().getAuthentication();
        final var user = (User) auth.getPrincipal();
        final var userInfoDTO = new ModelMapper().map(user, UserInfoResponseDTO.class);

        return ResponseEntity.ok().body(userInfoDTO);
    }
}
