package com.paperz.bikesanonymous.domain.csv;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Certification {
  @NotBlank private String bikeModel;
  @Indexed(unique = true)
  @NotBlank private String bikeSerialNumber;
  private long expeditionDateTS;
}
