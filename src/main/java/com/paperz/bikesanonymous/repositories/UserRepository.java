package com.paperz.bikesanonymous.repositories;

import com.paperz.bikesanonymous.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Mono<User> findUserByUsername(String username);

  Mono<Boolean> existsUserByUsername(String username);
}
