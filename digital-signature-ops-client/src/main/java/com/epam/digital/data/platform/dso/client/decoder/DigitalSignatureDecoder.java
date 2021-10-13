package com.epam.digital.data.platform.dso.client.decoder;

import com.epam.digital.data.platform.dso.api.dto.ErrorDto;
import com.epam.digital.data.platform.dso.client.exception.BadRequestException;
import com.epam.digital.data.platform.dso.client.exception.InternalServerErrorException;
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

  @SneakyThrows
  @Override
  public Exception decode(String methodKey, Response response) {
    if (response.status() == 400) {
      return new BadRequestException(objectMapper
          .readValue(Util.toByteArray(response.body().asInputStream()), objectMapper.constructType(
              ErrorDto.class)));
    }
    if (response.status() == 500) {
      return new InternalServerErrorException(objectMapper
          .readValue(Util.toByteArray(response.body().asInputStream()), objectMapper.constructType(
              ErrorDto.class)));
    }
    if (response.status() == 422) {
      return new SignatureValidationException(objectMapper
          .readValue(Util.toByteArray(response.body().asInputStream()), objectMapper.constructType(
              ErrorDto.class)));
    }
    return errorDecoderChain.decode(methodKey, response);
  }

}