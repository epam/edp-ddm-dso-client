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

package com.epam.digital.data.platform.dso.client.config;

import com.epam.digital.data.platform.dso.client.decoder.DigitalSignatureDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

/**
 * Feign client configuration class. Provides configuration for digital signature ops client
 */
public class DigitalOpsFeignConfig {

  /**
   * Returns error decoder {@link DigitalSignatureDecoder}
   *
   * @return error decoder for digital signature ops client
   */
  @Bean
  public DigitalSignatureDecoder digitalSignatureDecoder() {
    return new DigitalSignatureDecoder(new ObjectMapper());
  }

}