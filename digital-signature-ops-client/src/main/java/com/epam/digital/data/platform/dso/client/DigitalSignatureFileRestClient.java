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

import com.epam.digital.data.platform.dso.api.dto.SignFileRequestDto;
import com.epam.digital.data.platform.dso.api.dto.SignFileResponseDto;
import com.epam.digital.data.platform.dso.client.config.DigitalOpsFeignConfig;
import feign.error.ErrorHandling;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Rest client for digital signature file service. Processes requests for signing files in Ceph
 */
@FeignClient(name = "digital-signature-file-client", url = "${dso.url}/api/file", configuration = DigitalOpsFeignConfig.class)
public interface DigitalSignatureFileRestClient {

  /**
   * Creates signature for the specified file in Ceph bucket and replaces origin with signed one
   *
   * @param signFileRequest request object with key to Ceph data
   * @return sign response with boolean flag
   * @see SignFileResponseDto
   * @see SignFileRequestDto
   */
  @ErrorHandling
  @PostMapping("/sign")
  SignFileResponseDto sign(SignFileRequestDto signFileRequest);

}
