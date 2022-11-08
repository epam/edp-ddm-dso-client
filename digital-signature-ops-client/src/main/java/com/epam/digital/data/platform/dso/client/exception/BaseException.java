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
