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

package com.epam.digital.data.platform.dso.client.decoder;

import com.epam.digital.data.platform.dso.api.dto.ErrorDto;
import com.epam.digital.data.platform.dso.client.exception.BadRequestException;
import com.epam.digital.data.platform.dso.client.exception.InternalServerErrorException;
import com.epam.digital.data.platform.dso.client.exception.InvalidSignatureException;
import com.epam.digital.data.platform.dso.client.exception.SignatureValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;

/**
 * Error decoder for feign client, raises corresponding exception based on status
 */
public class DigitalSignatureDecoder implements ErrorDecoder {

  private final ObjectMapper objectMapper;
  private final ErrorDecoder errorDecoderChain = new Default();

  /**
   * Decoder constructor
   *
   * @param objectMapper for error constructing
   */
  public DigitalSignatureDecoder(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }


  @Override
  public Exception decode(String methodKey, Response response) {
    if (response.status() == 400) {
      return new BadRequestException(getErrorDto(response));
    }
    if (response.status() == 500) {
      return new InternalServerErrorException(getErrorDto(response));
    }
    if (response.status() == 422) {
      return new SignatureValidationException(getErrorDto(response));
    }
    if (response.status() == 412) {
      var error = getErrorDto(response);
      return new InvalidSignatureException(error.getMessage(), error);
    }
    return errorDecoderChain.decode(methodKey, response);
  }

  @SneakyThrows
  private ErrorDto getErrorDto(Response response) {
    return objectMapper
        .readValue(Util.toByteArray(response.body().asInputStream()), objectMapper.constructType(
            ErrorDto.class));
  }

}