package com.paperz.bikesanonymous.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PBKDF2Encoder implements PasswordEncoder {

  @Value("${password.encoder.secret}")
  private String secret;

  @Value("${password.encoder.iteration}")
  private Integer iteration;

  @Value("${password.encoder.keylength}")
  private Integer keyLength;

  @Override
  public String encode(CharSequence cs) {
    try {
      byte[] result =
          SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
              .generateSecret(
                  new PBEKeySpec(
                      cs.toString().toCharArray(), secret.getBytes(), iteration, keyLength))
              .getEncoded();
      return Base64.getEncoder().encodeToString(result);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public boolean matches(CharSequence cs, String string) {
    return encode(cs).equals(string);
  }
}
