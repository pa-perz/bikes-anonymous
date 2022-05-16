package com.paperz.bikesanonymous.controllers;

import com.paperz.bikesanonymous.domain.csv.Certification;
import com.paperz.bikesanonymous.repositories.CertificationRepository;
import com.paperz.bikesanonymous.utils.CSVUtil;
import com.paperz.bikesanonymous.utils.MailUtil;
import com.paperz.bikesanonymous.utils.PDFUtil;
import java.time.Instant;
import javax.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@Slf4j
public class CertificationController {
  CSVUtil csvUtil;
  CertificationRepository certificationRepository;
  MailUtil mailUtil;
  PDFUtil pdfUtil;

  @PostMapping(value = "/certification", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('USER')")
  public Mono<Certification> generateCertification(@RequestPart Mono<FilePart> file) {

    Mono<Certification.CertificationBuilder> certificationBuilderMono =
        file.map(filePart -> csvUtil.getCsvInformation(filePart));
    Mono<Certification> certification =
        certificationBuilderMono.map(
            certificationBuilder ->
                certificationBuilder.expeditionDateTS(Instant.now().toEpochMilli()).build());

    return certification.flatMap(
        c -> certificationRepository.save(c)).map(c -> {
      try {
        mailUtil.sendEmail("example@example.org", pdfUtil.certificationToPdf(c)
        );
      } catch (MessagingException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return c;
    });
  }
}
