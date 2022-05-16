package com.paperz.bikesanonymous.utils;

import com.paperz.bikesanonymous.domain.csv.Certification;
import com.paperz.bikesanonymous.domain.csv.Certification.CertificationBuilder;
import com.paperz.bikesanonymous.domain.csv.CertificationRow;
import com.paperz.bikesanonymous.domain.csv.CertificationRowHeader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class CSVUtil {

  public CertificationBuilder getCsvInformation(FilePart csv) {
    filenameValidation(csv);

    Flux<String> result = getCsvStrings(csv);

    return getBuilderWithBike(result);
  }

  private Flux<String> getCsvStrings(FilePart csv) {
    return csv.content().map(dataBuffer -> {
      byte[] bytes = new byte[dataBuffer.readableByteCount()];
      dataBuffer.read(bytes);
      DataBufferUtils.release(dataBuffer);
      return new String(bytes, StandardCharsets.UTF_8);
    });
  }

  private void filenameValidation(FilePart csv) {
    if (csv == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!isCsvFilename(csv.filename())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  private CertificationBuilder getBuilderWithBike(Flux<String> rowsFlux) {
    List<String> listRows;
    try {
      listRows = rowsFlux.collectList().toFuture().get();
    } catch (InterruptedException | ExecutionException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if (listRows == null || listRows.size() != 1) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    String csv = listRows.get(0);
    listRows = List.of(csv.split("\n"));

    if (listRows.size() != 2) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    CertificationRow bikeModelRow;
    CertificationRow bikeSerialNumberRow;
    try {

      String[] bikeModelSplit = listRows.get(0).split(",");
      if (bikeModelSplit.length != 2) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }

      CertificationRowHeader bikeModelHeader = CertificationRowHeader.valueOf(bikeModelSplit[0]);
      bikeModelRow = new CertificationRow(bikeModelHeader, bikeModelSplit[1]);

      String[] bikeSerialNumberSplit = listRows.get(1).split(",");
      if (bikeSerialNumberSplit.length != 2) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }

      CertificationRowHeader bikeSerialNumberHeader =
          CertificationRowHeader.valueOf(bikeSerialNumberSplit[0]);
      bikeSerialNumberRow = new CertificationRow(bikeSerialNumberHeader, bikeSerialNumberSplit[1]);
    } catch (IllegalArgumentException e) {
      log.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    return Certification.builder()
        .bikeModel(bikeModelRow.getValue())
        .bikeSerialNumber(bikeSerialNumberRow.getValue());
  }

  private boolean isCsvFilename(String filename) {
    return filename != null && filename.matches("^.*\\.csv$");
  }
}
