package com.paperz.bikesanonymous.utils;

import java.io.ByteArrayOutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MailUtil {

  private JavaMailSender javaMailSender;

  public void sendEmail(String recipientEmail, ByteArrayOutputStream certification) throws MessagingException {
    MimeMessage msg = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, true);
    helper.setTo(recipientEmail);
    helper.setSubject("Your cycling certification!");
    helper.setText("");
    MimeBodyPart messageBodyPart = new MimeBodyPart();
    DataSource dataSource = new ByteArrayDataSource(certification.toByteArray(), "application/pdf");
    messageBodyPart.setHeader("Content-Transfer-Encoding", "base64");
    messageBodyPart.setDataHandler(new DataHandler(dataSource));
    messageBodyPart.setFileName("test.pdf");
    helper.addAttachment("certification.pdf", new ByteArrayResource(certification.toByteArray()));
    javaMailSender.send(msg);
  }
}
