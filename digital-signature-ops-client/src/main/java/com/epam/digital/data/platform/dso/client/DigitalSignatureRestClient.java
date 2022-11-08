/*
 * Copyright 2021 EPAM Systems.
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

package com.epam.digital.data.platform.dso.client;

import com.epam.digital.data.platform.dso.api.dto.OwnerResponseDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationRequestDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationResponseDto;
import com.epam.digital.data.platform.dso.api.dto.VerifySubjectRequestDto;
import com.epam.digital.data.platform.dso.api.dto.VerifySubjectResponseDto;
import com.epam.digital.data.platform.dso.client.config.DigitalOpsFeignConfig;
import feign.error.ErrorHandling;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * Rest client for digital signature ops service. Processes requests related to user created
 * signature
 */
@FeignClient(name = "digital-signature-client", url = "${dso.url}/api/esignature", configuration = DigitalOpsFeignConfig.class)
public interface DigitalSignatureRestClient {

  /**
   * Verifies signature validity for officer
   *
   * @param request request object with signature and data to verify
   * @return verification response or error
   * @see VerificationRequestDto
   * @see VerificationResponseDto
   */
  @ErrorHandling
  @PostMapping("/officer/verify")
  VerificationResponseDto verifyOfficer(VerificationRequestDto request);

  /**
   * Verifies signature validity for citizen
   *
   * @param request request object with signature and data to verify
   * @return verification response or error
   * @see VerifySubjectResponseDto
   * @see VerifySubjectRequestDto
   */
  @ErrorHandling
  @PostMapping("/citizen/verify")
  VerifySubjectResponseDto verifyCitizen(VerifySubjectRequestDto request);


  /**
   * Returns signature owner info verifies auth timeout
   *
   * @param verifyRequestDto request object with signature and data
   * @return signature owner info
   * @see OwnerResponseDto
   */
  @ErrorHandling
  @PostMapping("/owner")
  OwnerResponseDto getOwner(VerificationRequestDto verifyRequestDto);

  /**
   * Returns signature owner info
   *
   * @param verifyRequestDto request object with signature and data
   * @return signature owner info
   * @see OwnerResponseDto
   */
  @ErrorHandling
  @PostMapping("/owner-infinite")
  OwnerResponseDto getOwnerInfinite(VerificationRequestDto verifyRequestDto);
}
