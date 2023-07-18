package com.emre.controller;

import com.emre.dto.request.*;
import com.emre.dto.response.RegisterResponseDto;
import com.emre.repository.entity.Auth;
import com.emre.repository.entity.enums.ERole;
import com.emre.service.AuthService;
import com.emre.utility.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.emre.constant.ApiUrls.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping(REGISTER + "MD5")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.registerMD5(dto));
    }

    @PostMapping(REGISTER + "2")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }

    @PostMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateRequestDto dto) {
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
    @PostMapping(LOGIN+"/Md5")
    public ResponseEntity<String> loginWithMD5(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.loginMD5(dto));
    }

    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Auth>> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }

    @PostMapping("/update-email-username")
    public ResponseEntity<Boolean> updateAuth(@RequestBody UpdateEmailOrUsernameRequestDto dto) {
        return ResponseEntity.ok(authService.updateAuth(dto));
    }

    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<Boolean> delete(String token) {
        return ResponseEntity.ok(authService.delete(token));
    }

    @GetMapping("/create-token-with-id")
    public ResponseEntity<String> createToken(Long id) {
        return ResponseEntity.ok(tokenProvider.createToken(id).get());
    }

    @GetMapping("/create-token-with-role")
    public ResponseEntity<String> createToken(Long id, ERole role) {
        return ResponseEntity.ok(tokenProvider.createToken(id, role).get());
    }

    @GetMapping("/get-id-from-token")
    public ResponseEntity<Long> getIdFromToken(String token) {
        return ResponseEntity.ok(tokenProvider.getIdFromToken(token).get());
    }

    @GetMapping("/get-role-from-token")
    public ResponseEntity<String> getRoleFromToken(String token) {
        return ResponseEntity.ok(tokenProvider.getRoleFromToken(token).get());
    }
/*          Bunlar redis için örnekti.
    @GetMapping("/redis")
    @Cacheable(value = "redisexample")
    public String redisExample(String value) {
        try {
            Thread.sleep(2000);
            return value;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @GetMapping("/cache-delete")
    @CacheEvict(cacheNames = "redisexample", allEntries = true)
    public void cacheDelete() {

    }
 */

    @GetMapping("/find-by-role/{role}")
    public ResponseEntity<List<Long>> findByRole(@PathVariable String role) {
        return ResponseEntity.ok(authService.findByRole(role));
    }
    @PutMapping(CHANGE_PASSWORD)
    public ResponseEntity<Boolean> changePassword(@RequestBody ToAuthChangePasswordDto dto) {
        return ResponseEntity.ok(authService.changePassword(dto));
    }

    @PostMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(String email,String username) {
        return ResponseEntity.ok(authService.forgotPassword(email,username));
    }
}
