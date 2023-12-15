package br.com.tce.carsystem.service;

import br.com.tce.carsystem.domain.user.User;
import br.com.tce.carsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 60000)
    public void checkCurrentLoggedUser() {

        final var optionalUser = userRepository.findFirstByLastLoginIsNotNullOrderByLastLogin();

        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            log.info("Last logged user -> {} at {}",
                    user.getLogin(), user.getLastLogin());
        } else {
            log.info("No user has been logged so far!");
        }
    }
}
