package com.epam.digital.data.platform.dso.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for verification error details
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class ErrorDto {

  /**
   * Numerical code for occurred error defined in internal implementation of signature service
   */
  public String code;
  /**
   * Message related to current error code
   */
  public String message;
  /**
   * Localized message related to current error code
   */
  public String localizedMessage;

}
