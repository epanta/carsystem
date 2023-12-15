package br.com.tce.carsystem.infra.security;

import br.com.tce.carsystem.utils.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .exceptionHandling((exception)-> exception.authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedPage("/error/accedd-denied"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/cars").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/cars").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/cars").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/cars/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/cars/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/cars/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/cars/{id}").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/api/me").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/signin").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/signin").permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/favicon.ico/**").permitAll()

                        .requestMatchers( "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
