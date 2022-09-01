package com.epam.digital.data.platform.dso.client.exception;

import com.epam.digital.data.platform.dso.api.dto.ErrorDto;
import feign.error.FeignExceptionConstructor;

/**
 * Class represents an exception thrown in case when internal server exception occurred
 */
public class InternalServerErrorException extends BaseException {

  /**
   * Constructor for exception
   *
   * @param errorDto error data transfer object with error specification
   */
  @FeignExceptionConstructor

  public InternalServerErrorException(ErrorDto errorDto) {
    super(errorDto);
  }
}
