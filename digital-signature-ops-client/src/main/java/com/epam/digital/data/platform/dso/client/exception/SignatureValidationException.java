package com.epam.digital.data.platform.dso.client.exception;

import com.epam.digital.data.platform.dso.api.dto.ErrorDto;

/**
 * Class represents an exception thrown in case when signature validation failed
 */
public class SignatureValidationException extends BaseException{

  /**
   * Constructor for base exception
   *
   * @param errorDto error data transfer object with error specification
   */
  public SignatureValidationException(ErrorDto errorDto) {
    super(errorDto);
  }
}
