package com.epam.digital.data.platform.dso.client.exception;

import com.epam.digital.data.platform.dso.api.dto.ErrorDto;
import feign.error.FeignExceptionConstructor;

/**
 * Class represents an exception thrown in case of inability of processing request
 */
public class BadRequestException extends BaseException {

  /**
   * Constructor for exception
   *
   * @param errorDto error data transfer object with error specification
   */
  @FeignExceptionConstructor
  public BadRequestException(ErrorDto errorDto) {
    super(errorDto);
  }
}
