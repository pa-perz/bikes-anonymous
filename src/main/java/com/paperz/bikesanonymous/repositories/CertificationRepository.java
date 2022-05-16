package com.paperz.bikesanonymous.repositories;

import com.paperz.bikesanonymous.domain.csv.Certification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CertificationRepository extends ReactiveMongoRepository<Certification, String> {}
