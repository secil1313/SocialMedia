package com.emre.manager;

import com.emre.dto.response.ForgotPasswordMailResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.emre.constant.ApiUrls.FORGOT_PASSWORD;

@FeignClient(name = "auth-mail", url = "http://localhost:8085/api/v1/mail")
public interface IEMailManager {
    @PostMapping(FORGOT_PASSWORD)
    ResponseEntity<Boolean> forgotPasswordMail(@RequestBody ForgotPasswordMailResponseDto dto);
}
