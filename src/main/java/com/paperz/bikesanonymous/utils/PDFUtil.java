package com.paperz.bikesanonymous.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.paperz.bikesanonymous.domain.csv.Certification;
import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PDFUtil {

  public ByteArrayOutputStream certificationToPdf(Certification certification) {
    try {
      Document document = new Document();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter.getInstance(document, baos);

      document.open();
      Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

      document.addAuthor("Bikes Anonymous");
      document.addTitle("Cycling certification");

      Chunk bike = new Chunk(String.format("%s -- %s", certification.getBikeModel(),
        certification.getBikeSerialNumber()));
      Chunk date = new Chunk(String.valueOf(certification.getExpeditionDateTS()), font);
      document.add(bike);
      document.add(date);

      document.close();
      return baos;

    } catch (DocumentException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
