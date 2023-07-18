package com.emre.manager;

import com.emre.dto.request.ChangePasswordRequestDto;
import com.emre.dto.request.ToAuthChangePasswordDto;
import com.emre.dto.request.UpdateEmailOrUsernameRequestDto;
import com.emre.repository.entity.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-profile-auth",url = "http://localhost:8090/api/v1/auth")
public interface IAuthManager {
    @PostMapping("/update-email-username")
    ResponseEntity<Boolean> updateEmailOrUsername(@RequestBody UpdateEmailOrUsernameRequestDto dto);

    //auth service e gidip rollere bakacak. ona göre id lerini getirecek.
    @GetMapping("/find-by-role/{role}")
    ResponseEntity<List<Long>> findByRole(@PathVariable String role);

    @PutMapping("/change-password")
    ResponseEntity<Boolean> changePassword(@RequestBody ToAuthChangePasswordDto dto);
}
