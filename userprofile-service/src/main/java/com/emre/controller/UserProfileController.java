package com.emre.controller;

import com.emre.dto.request.ChangePasswordRequestDto;
import com.emre.dto.request.NewCreateUserRequestDto;
import com.emre.dto.request.UserProfileUpdateRequestDto;
import com.emre.dto.response.UserProfileChangePasswordResponseDto;
import com.emre.repository.entity.UserProfile;
import com.emre.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.emre.constant.ApiUrls.*;

@RestController
@RequestMapping(USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestHeader(value = "Authorization") String token,@RequestBody NewCreateUserRequestDto dto) {
        return ResponseEntity.ok(userProfileService.createUser(token,dto));
    }

    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<List<UserProfile>> findAll() {
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @GetMapping(ACTIVATE_STATUS + "/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @PostMapping(UPDATE)
    public ResponseEntity<UserProfile> update(@RequestBody UserProfileUpdateRequestDto dto) {
        return ResponseEntity.ok(userProfileService.update(dto));
    }

    @DeleteMapping(DELETE_BY_ID + "/{authId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.delete(authId));
    }

    @GetMapping("/find-username-cache/{username}")
    public ResponseEntity<UserProfile> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }

    @GetMapping("/find-by-role/{role}")
    public ResponseEntity<List<UserProfile>> findByRole(@PathVariable String role) {
        return ResponseEntity.ok(userProfileService.findByRole(role));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(ChangePasswordRequestDto dto) {
        return ResponseEntity.ok(userProfileService.changePassword(dto));
    }

    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(@RequestBody UserProfileChangePasswordResponseDto dto) {
        return ResponseEntity.ok(userProfileService.forgotPassword(dto));
    }

    //PostService'den authId getirmek için yazdık.(Feign Client)
    @GetMapping("/find-by-auth-id/{authId}")
    public ResponseEntity<Optional<UserProfile>> findByAuthId(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.findByAuthId(authId));
    }
}
