package com.epam.digital.data.platform.dso.client.exception;

import com.epam.digital.data.platform.dso.api.dto.ErrorDto;
import lombok.Getter;

/**
 * Class represents base exception containing {@link ErrorDto}. Describes occurred error in case
 * when the server cannot process the client request.
 */
@Getter
public class BaseException extends RuntimeException {

  private final ErrorDto errorDto;

  /**
   * Constructor for base exception
   *
   * @param errorDto error data transfer object with error specification
   */
  public BaseException(ErrorDto errorDto) {
    this.errorDto = errorDto;
  }
}
