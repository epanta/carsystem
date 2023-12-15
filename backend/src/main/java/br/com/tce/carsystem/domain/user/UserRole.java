package br.com.tce.carsystem.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserRole {

    ADMIN("ADMIN"),
    USER("USER");

    @Getter
    private final String role;
}
