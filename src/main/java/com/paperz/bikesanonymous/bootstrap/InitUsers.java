package com.paperz.bikesanonymous.bootstrap;

import com.paperz.bikesanonymous.domain.Role;
import com.paperz.bikesanonymous.domain.User;
import com.paperz.bikesanonymous.repositories.UserRepository;
import com.paperz.bikesanonymous.security.PBKDF2Encoder;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Component
@Slf4j
public class InitUsers implements CommandLineRunner {

  private final UserRepository userRepository;
  private PBKDF2Encoder passwordEncoder;

  @Override
  public void run(String... args) {
    userRepository
        .deleteAll()
        .thenMany(
            Flux.just(
                    User.builder()
                        .username("user@ba.com")
                        .password(passwordEncoder.encode("pass"))
                        .enabled(true)
                        .roles(Set.of(Role.ROLE_USER))
                        .build(),
                    User.builder()
                        .username("admin@ba.com")
                        .password(passwordEncoder.encode("pass"))
                        .enabled(true)
                        .roles(Set.of(Role.ROLE_ADMIN))
                        .build())
                .flatMap(userRepository::save))
        .subscribe(
            null,
            null,
            () -> userRepository.findAll().subscribe(user -> log.info(user.toString())));
  }
}
