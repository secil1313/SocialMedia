package com.emre.manager;

import com.emre.dto.request.NewCreateUserRequestDto;
import com.emre.dto.request.UserProfileChangePassword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.emre.constant.ApiUrls.DELETE_BY_ID;
import static com.emre.constant.ApiUrls.FORGOT_PASSWORD;

@FeignClient(name = "auth-user-profile", url = "http://localhost:8080/api/v1/user-profile")
public interface IUserProfileManager {
    @PostMapping("/create")
    ResponseEntity<Boolean> createUser(@RequestHeader(value = "Authorization") String token, @RequestBody NewCreateUserRequestDto dto);

    @GetMapping("/activate-status/{authId}")
    ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);

    @DeleteMapping(DELETE_BY_ID + "/{authId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long authId);

    @PutMapping(FORGOT_PASSWORD)
        //Unutulup, Değişen Şifrenin aktarılması için
    ResponseEntity<Boolean> forgotPassword(@RequestBody UserProfileChangePassword dto);
}
