package com.epam.digital.data.platform.dso.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for signature owner response
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerResponseDto {

  private String fullName;
  private String drfo;
  private String edrpou;

}
