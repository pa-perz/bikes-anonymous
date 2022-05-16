package com.paperz.bikesanonymous.services;

import com.paperz.bikesanonymous.domain.User;
import com.paperz.bikesanonymous.domain.auth.AuthRequest;
import com.paperz.bikesanonymous.repositories.UserRepository;
import com.paperz.bikesanonymous.security.PBKDF2Encoder;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private PBKDF2Encoder passwordEncoder;

  public Mono<User> findByUsername(String username) {
    return userRepository.findUserByUsername(username);
  }

  public Mono<Boolean> userExists(String username) {
    return userRepository.existsUserByUsername(username);
  }

  public Mono<User> createUser(AuthRequest authRequest) {
    User newUser =
        User.builder()
            .username(authRequest.getUsername())
            .password(passwordEncoder.encode(authRequest.getPassword()))
            .build();
    return userRepository
        .insert(newUser)
        .onErrorMap(
            DuplicateKeyException.class,
            e -> new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists."));
  }
}
