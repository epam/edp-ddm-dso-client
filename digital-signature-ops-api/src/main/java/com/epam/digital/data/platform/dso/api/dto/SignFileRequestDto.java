package com.epam.digital.data.platform.dso.api.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignFileRequestDto {

  @NotNull
  private String cephKey;

}
