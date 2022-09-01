package com.epam.digital.data.platform.dso.client;

import com.epam.digital.data.platform.dso.api.dto.OwnerResponseDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationRequestDto;
import com.epam.digital.data.platform.dso.api.dto.VerificationResponseDto;
import com.epam.digital.data.platform.dso.api.dto.VerifySubjectRequestDto;
import com.epam.digital.data.platform.dso.api.dto.VerifySubjectResponseDto;
import com.epam.digital.data.platform.dso.client.config.DigitalOpsFeignConfig;
import feign.error.ErrorHandling;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * Rest client for digital signature ops service. Processes requests related to user created
 * signature
 */
@FeignClient(name = "digital-signature-client", url = "${dso.url}/api/esignature", configuration = DigitalOpsFeignConfig.class)
public interface DigitalSignatureRestClient {

  /**
   * Verifies signature validity for officer
   *
   * @param request request object with signature and data to verify
   * @return verification response or error
   * @see VerificationRequestDto
   * @see VerificationResponseDto
   */
  @ErrorHandling
  @PostMapping("/officer/verify")
  VerificationResponseDto verifyOfficer(VerificationRequestDto request);

  /**
   * Verifies signature validity for citizen
   *
   * @param request request object with signature and data to verify
   * @return verification response or error
   * @see VerifySubjectResponseDto
   * @see VerifySubjectRequestDto
   */
  @ErrorHandling
  @PostMapping("/citizen/verify")
  VerifySubjectResponseDto verifyCitizen(VerifySubjectRequestDto request);


  /**
   * Returns signature owner info verifies auth timeout
   *
   * @param verifyRequestDto request object with signature and data
   * @return signature owner info
   * @see OwnerResponseDto
   */
  @ErrorHandling
  @PostMapping("/owner")
  OwnerResponseDto getOwner(VerificationRequestDto verifyRequestDto);

  /**
   * Returns signature owner info
   *
   * @param verifyRequestDto request object with signature and data
   * @return signature owner info
   * @see OwnerResponseDto
   */
  @ErrorHandling
  @PostMapping("/owner-infinite")
  OwnerResponseDto getOwnerInfinite(VerificationRequestDto verifyRequestDto);
}
