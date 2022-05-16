package com.paperz.bikesanonymous.domain.csv;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CertificationRow {
  @NotNull private CertificationRowHeader name;
  @NotBlank private String value;
}
