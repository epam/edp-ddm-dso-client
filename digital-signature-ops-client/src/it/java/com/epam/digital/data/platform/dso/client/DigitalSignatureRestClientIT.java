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

package com.epam.digital.data.platform.dso.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.digital.data.platform.dso.api.dto.ContentDto;
import com.epam.digital.data.platform.dso.api.dto.ErrorDto;
import com.epam.digital.data.platform.dso.api.dto.OwnerResponseDto;
import com.epam.digital.data.platform.dso.api.dto.SignDataResponseDto;
import com.epam.digital.data.platform.dso.api.dto.SignDetailsDto;
import com.epam.digital.data.platform.dso.api.dto.SignFormat;
import com.epam.digital.data.platform.dso.api.dto.SignInfoRequestDto;
import com.epam.digital.data.platform.dso.api.dto.SignatureInfoResponseDto;
import com.epam.digital.data.platform.dso.api.dto.ValidationResponseDto;
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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpHeaders;

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
    assertEquals(internalError, exception.getErrorDto().getMessage());
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

  @Test
  void validateSignData() throws JsonProcessingException {
    var httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
    httpHeaders.add("X-Access-Token", "token");
    var expectedResult = new ValidationResponseDto(true, SignFormat.ASIC, null);
    var request = new SignInfoRequestDto("signature", SignFormat.ALL);
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + "/validate"))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(request)))
            .withHeader("Content-Type", equalTo("application/json"))
            .withHeader("X-Access-Token", equalTo("token"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(objectMapper.writeValueAsString(expectedResult)))
        )
    );

    var result = digitalSignatureRestClient.validate(request, httpHeaders);

    assertEquals(expectedResult.isValid(), result.isValid());
    assertEquals(expectedResult.getContainer(), result.getContainer());
    assertThat(result.getError()).isNull();
  }

  @Test
  void getSignContent() throws JsonProcessingException {
    var httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
    httpHeaders.add("X-Access-Token", "token");
    var contents = List.of(new ContentDto("data1", "test.txt"),
        new ContentDto("data2", "test2.txt"));
    var expectedResult = new SignDataResponseDto(contents);
    var request = new SignInfoRequestDto("signature", SignFormat.ASIC);
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + "/content"))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(request)))
            .withHeader("Content-Type", equalTo("application/json"))
            .withHeader("X-Access-Token", equalTo("token"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(objectMapper.writeValueAsString(expectedResult)))
        )
    );

    var result = digitalSignatureRestClient.content(request, httpHeaders).getContent();

    assertEquals(expectedResult.getContent().size(), result.size());
    assertEquals(expectedResult.getContent().get(0), result.get(0));
    assertEquals(expectedResult.getContent().get(1), result.get(1));
  }

  @Test
  void getSignInfo() throws JsonProcessingException {
    var httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
    httpHeaders.add("X-Access-Token", "token");
    var signDetails = new SignDetailsDto();
    signDetails.setSubjFullName("fullName");
    var expectedResult = new SignatureInfoResponseDto(List.of(signDetails));
    var request = new SignInfoRequestDto("signature", SignFormat.CADES);
    restClientWireMock.addStubMapping(
        stubFor(post(urlEqualTo(BASE_URL + "/info"))
            .withRequestBody(equalTo(objectMapper.writeValueAsString(request)))
            .withHeader("Content-Type", equalTo("application/json"))
            .withHeader("X-Access-Token", equalTo("token"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(objectMapper.writeValueAsString(expectedResult)))
        )
    );

    var result = digitalSignatureRestClient.info(request, httpHeaders).getInfo();

    assertThat(result.size()).isOne();
    assertEquals(expectedResult.getInfo().get(0), result.get(0));
  }
}
