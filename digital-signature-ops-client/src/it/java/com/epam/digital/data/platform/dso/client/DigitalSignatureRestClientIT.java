package com.epam.digital.data.platform.dso.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.digital.data.platform.dso.api.dto.ErrorDto;
import com.epam.digital.data.platform.dso.api.dto.OwnerResponseDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationRequestDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationResponseDto;
import com.epam.digital.data.platform.dso.client.config.DigitalOpsFeignConfig;
import com.epam.digital.data.platform.dso.client.exception.BadRequestException;
import com.epam.digital.data.platform.dso.client.exception.InternalServerErrorException;
import com.epam.digital.data.platform.dso.client.exception.InvalidSignatureException;
import com.epam.digital.data.platform.dso.client.exception.SignatureValidationException;
import com.epam.digital.data.platform.dso.client.it.config.WireMockConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootTest(classes = {DigitalOpsFeignConfig.class, WireMockConfig.class})
@EnableAutoConfiguration
@EnableFeignClients(clients = {DigitalSignatureRestClient.class})
class DigitalSignatureRestClientIT {

  private static final String BASE_URL = "/api/esignature";
  private static final String VERIFY = "/officer/verify";
  private static final String OWNER = "/owner";
  private static final String OWNER_INFINITE = "/owner-infinite";
  private static final VerificationRequestDto REQ = new VerificationRequestDto("signature", "data");
  
  @Autowired
  protected WireMockServer restClientWireMock;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  private DigitalSignatureRestClient digitalSignatureRestClient;

  @Test
  void testPositive() throws JsonProcessingException {
    VerificationResponseDto resp = new VerificationResponseDto(true, null);
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + VERIFY))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(REQ)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(objectMapper.writeValueAsString(resp)))
        )
    );
    
    VerificationResponseDto verifyResponseDto = digitalSignatureRestClient.verifyOfficer(REQ);
    
    assertThat(verifyResponseDto.isValid()).isTrue();
  }

  @Test
  void badRequestRaisesException() throws JsonProcessingException {
    String badRequest = "badRequest";
    ErrorDto resp = ErrorDto.builder().message(badRequest).build();
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + VERIFY))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(REQ)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(400)
                .withBody(objectMapper.writeValueAsString(resp)))
        )
    );
    
    BadRequestException badRequestException = assertThrows(BadRequestException.class,
        () -> digitalSignatureRestClient.verifyOfficer(REQ));
    
    assertEquals(badRequest, badRequestException.getErrorDto().message);
  }

  @Test
  void internalErrorRaisesException() throws JsonProcessingException {
    String internalError = "internalError";
    ErrorDto resp = ErrorDto.builder().message(internalError).build();
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + VERIFY))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(REQ)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(500)
                .withBody(objectMapper.writeValueAsString(resp)))
        )
    );
    
    InternalServerErrorException badRequestException = assertThrows(
        InternalServerErrorException.class, () -> digitalSignatureRestClient.verifyOfficer(REQ));
    
    assertEquals(internalError, badRequestException.getErrorDto().message);
  }

  @Test
  void invalidSignatureException() throws JsonProcessingException {
    String internalError = "internalError";
    ErrorDto resp = ErrorDto.builder().message(internalError).build();
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + VERIFY))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(REQ)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(412)
                .withBody(objectMapper.writeValueAsString(resp)))
        )
    );

    InvalidSignatureException exception = assertThrows(
        InvalidSignatureException.class, () -> digitalSignatureRestClient.verifyOfficer(REQ));

    assertEquals(internalError, exception.getMessage());
  }

  @Test
  void getOwner() throws JsonProcessingException {
    OwnerResponseDto owner = new OwnerResponseDto("fullName", "drfo", "edrpou");
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + OWNER))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(REQ)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(objectMapper.writeValueAsString(owner)))
        )
    );
    
    OwnerResponseDto actOwner = digitalSignatureRestClient.getOwner(REQ);
    
    assertEquals(actOwner.getFullName(), owner.getFullName());
    assertEquals(actOwner.getDrfo(), owner.getDrfo());
    assertEquals(actOwner.getEdrpou(), owner.getEdrpou());
  }

  @Test
  void getOwnerInfiniteUnprocesableEntityRaisesExc() throws JsonProcessingException{
    ErrorDto resp = ErrorDto.builder().message("msg").code("code").build();
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + OWNER_INFINITE))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(REQ)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(422)
                .withBody(objectMapper.writeValueAsString(resp)))
        )
    );
    
    SignatureValidationException exception = assertThrows(
        SignatureValidationException.class,() -> digitalSignatureRestClient.getOwnerInfinite(REQ));
    
    assertEquals("msg", exception.getErrorDto().message);
    assertEquals("code", exception.getErrorDto().code);
  }

  @Test
  void getOwnerInfinite() throws JsonProcessingException {
    OwnerResponseDto owner = new OwnerResponseDto("fullName", "drfo", "edrpou");
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + OWNER_INFINITE))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(REQ)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(objectMapper.writeValueAsString(owner)))
        )
    );
    
    OwnerResponseDto actOwner = digitalSignatureRestClient.getOwnerInfinite(REQ);
    
    assertEquals(actOwner.getFullName(), owner.getFullName());
    assertEquals(actOwner.getDrfo(), owner.getDrfo());
    assertEquals(actOwner.getEdrpou(), owner.getEdrpou());
  }
}
