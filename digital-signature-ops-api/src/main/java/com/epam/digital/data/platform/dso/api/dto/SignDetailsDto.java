/*
 * Copyright 2023 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.digital.data.platform.dso.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object representing detailed information about the signer.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignDetailsDto {

  private String issuer;
  private String issuerCN;
  private String serial;
  private String subject;
  private String subjCN;
  private String subjOrg;
  private String subjOrgUnit;
  private String subjTitle;
  private String subjState;
  private String subjLocality;
  private String subjFullName;
  private String subjAddress;
  private String subjPhone;
  private String subjEMail;
  private String subjDNS;
  private String subjEDRPOUCode;
  private String subjDRFOCode;
}
