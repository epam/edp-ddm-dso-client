package com.epam.digital.data.platform.dso.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for imposed seal
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignResponseDto {

  /**
   * String containing signature
   */
  public String signature;

}
