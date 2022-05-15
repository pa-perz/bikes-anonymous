package com.paperz.bikesanonymous;

import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BikesAnonymousApplicationTests {

  @Test
  void contextLoads() {
    Assertions.assertNotNull(this);
  }
}
