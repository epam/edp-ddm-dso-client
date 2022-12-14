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

import com.epam.digital.data.platform.dso.api.dto.SignRequestDto;
import com.epam.digital.data.platform.dso.api.dto.SignResponseDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationRequestDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationResponseDto;
import com.epam.digital.data.platform.dso.client.config.DigitalOpsFeignConfig;
import feign.error.ErrorHandling;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Rest client for digital signature ops service. Processes requests related to system signature
 * (seal).
 */
@FeignClient(name = "digital-seal-client", url = "${dso.url}/api/eseal", configuration = DigitalOpsFeignConfig.class)
public interface DigitalSealRestClient {

  /**
   * Verifies digital seal imposed on document
   *
   * @param verifyRequestDto request object containing signature and data
   * @return verification response or error
   * @see VerificationRequestDto
   * @see VerificationResponseDto
   */
  @ErrorHandling
  @PostMapping("/verify")
  VerificationResponseDto verify(VerificationRequestDto verifyRequestDto);

  /**
   * Creates system signature (seal) based on passed request data
   *
   * @param signRequest request object containing data to sign
   * @return signature created by system key
   * @see SignRequestDto
   * @see SignResponseDto
   */
  @ErrorHandling
  @PostMapping("/sign")
  SignResponseDto sign(SignRequestDto signRequest);

  /**
   * Creates system signature (seal) based on passed request data
   *
   * @param signRequest request object containing data to sign
   * @param headers http headers
   * @return signature created by system key
   * @see SignRequestDto
   * @see SignResponseDto
   */
  @ErrorHandling
  @PostMapping("/sign")
  SignResponseDto sign(SignRequestDto signRequest,  @RequestHeader HttpHeaders headers);

}
