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