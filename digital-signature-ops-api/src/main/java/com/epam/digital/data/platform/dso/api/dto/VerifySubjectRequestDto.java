package com.epam.digital.data.platform.dso.api.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for verification request with defined list of allowed subjects
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifySubjectRequestDto {

  @NotNull
  private List<Subject> allowedSubjects;
  @NotNull
  private String signature;
  @NotNull
  private String data;

}
