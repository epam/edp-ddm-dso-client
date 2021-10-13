package com.epam.digital.data.platform.dso.api.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object used for signature verification request
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequestDto {

  @NotNull
  private String signature;
  @NotNull
  private String data;

}

