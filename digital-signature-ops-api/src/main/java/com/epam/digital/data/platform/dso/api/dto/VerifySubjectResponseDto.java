package com.epam.digital.data.platform.dso.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for subject signature verification response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifySubjectResponseDto {

  private boolean isValid;
  private ErrorDto error;

}
