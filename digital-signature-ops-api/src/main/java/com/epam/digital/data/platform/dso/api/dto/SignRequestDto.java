package com.epam.digital.data.platform.dso.api.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for signing request
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignRequestDto {

  /**
   * String representation of data to be signed
   */
  @NotNull
  public String data;

}
