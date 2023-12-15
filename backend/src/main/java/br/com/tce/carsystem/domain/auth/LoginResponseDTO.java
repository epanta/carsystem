package br.com.tce.carsystem.domain.auth;

public record LoginResponseDTO(String token, String login, String id, String role) {
}
