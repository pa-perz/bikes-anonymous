package com.paperz.bikesanonymous.configurations;

import com.paperz.bikesanonymous.security.AuthenticationManager;
import com.paperz.bikesanonymous.security.SecurityContextRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

  private AuthenticationManager authenticationManager;
  private SecurityContextRepository securityContextRepository;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http.exceptionHandling()
        .authenticationEntryPoint(
            (swe, e) ->
                Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
        .accessDeniedHandler(
            (swe, e) ->
                Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
        .and()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .httpBasic()
        .disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .pathMatchers("/login", "/register")
        .permitAll()
        .pathMatchers(
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**")
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .build();
  }
}
