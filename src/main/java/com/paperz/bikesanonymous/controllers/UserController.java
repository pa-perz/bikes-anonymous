package com.paperz.bikesanonymous.controllers;

import com.paperz.bikesanonymous.domain.auth.AuthMessage;
import com.paperz.bikesanonymous.domain.auth.AuthRequest;
import com.paperz.bikesanonymous.domain.auth.AuthResponse;
import com.paperz.bikesanonymous.security.PBKDF2Encoder;
import com.paperz.bikesanonymous.services.UserService;
import com.paperz.bikesanonymous.utils.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class UserController {
  private JWTUtil jwtUtil;
  private PBKDF2Encoder passwordEncoder;
  private UserService userService;

  @PostMapping("/login")
  public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
    return userService
        .findByUsername(ar.getUsername())
        .filter(
            userDetails ->
                passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
        .map(
            userDetails ->
                ResponseEntity.ok(
                    AuthResponse.builder().token(jwtUtil.generateToken(userDetails)).build()))
        .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
  }

  @PostMapping("/register")
  public Mono<ResponseEntity<AuthResponse>> register(@RequestBody AuthRequest ar) {
    return userService
        .createUser(ar)
        .map(
            user ->
                ResponseEntity.ok(
                    AuthResponse.builder().token(jwtUtil.generateToken(user)).build()));
  }

  @GetMapping("/hello/user")
  @PreAuthorize("hasRole('USER')")
  public Mono<ResponseEntity<AuthMessage>> helloUser() {
    return Mono.just(ResponseEntity.ok(AuthMessage.builder().content("Hello user!").build()));
  }

  @GetMapping("/hello/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<ResponseEntity<AuthMessage>> helloAdmin() {
    return Mono.just(ResponseEntity.ok(AuthMessage.builder().content("Hello admin!").build()));
  }

  @GetMapping("/hello")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public Mono<ResponseEntity<AuthMessage>> hello() {
    return Mono.just(ResponseEntity.ok(AuthMessage.builder().content("Hello!").build()));
  }
}
