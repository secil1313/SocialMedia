package com.emre.service;

import com.emre.dto.request.*;
import com.emre.dto.response.ForgotPasswordMailResponseDto;
import com.emre.dto.response.RegisterResponseDto;
import com.emre.exception.AuthManagerException;
import com.emre.exception.ErrorType;
import com.emre.manager.IEMailManager;
import com.emre.manager.IUserProfileManager;
import com.emre.mapper.IAuthMapper;
import com.emre.rabbitmq.producer.RegisterMailProducer;
import com.emre.rabbitmq.producer.RegisterProducer;
import com.emre.repository.IAuthRepository;
import com.emre.repository.entity.Auth;
import com.emre.repository.entity.enums.ERole;
import com.emre.repository.entity.enums.EStatus;
import com.emre.utility.CodeGenerator;
import com.emre.utility.JwtTokenProvider;
import com.emre.utility.MD5Encoding;
import com.emre.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final IUserProfileManager userProfileManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegisterProducer registerProducer;
    private final RegisterMailProducer registerMailProducer;
    private final PasswordEncoder passwordEncoder;
    private final IEMailManager eMailManager;

    public AuthService(IAuthRepository authRepository,
                       IUserProfileManager userProfileManager,
                       JwtTokenProvider jwtTokenProvider,
                       RegisterProducer registerProducer,
                       RegisterMailProducer registerMailProducer,
                       PasswordEncoder passwordEncoder,
                       IEMailManager eMailManager) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userProfileManager = userProfileManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.registerProducer = registerProducer;
        this.registerMailProducer = registerMailProducer;
        this.passwordEncoder = passwordEncoder;
        this.eMailManager = eMailManager;
    }

    public RegisterResponseDto registerMD5(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        if (dto.getPassword().equals(dto.getRepassword())) {
            auth.setActivationCode(CodeGenerator.generateCode());
            //auth.setPassword(MD5Encoding.md5(dto.getPassword())); //md5 şifreleme yönetimini denemek için.
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            String token = jwtTokenProvider.createToken(auth.getId(), auth.getRole()).get();
            userProfileManager.createUser("Bearer " + token, IAuthMapper.INSTANCE.fromAuthToNewCreateUserDto(auth));
        } else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        return IAuthMapper.INSTANCE.toRegisterResponse(auth);
    }

    public Boolean activateStatus(ActivateRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        } else if (!auth.get().getActivationCode().equals(dto.getActivateCode())) {
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
        auth.get().setStatus(EStatus.ACTIVE);
        update(auth.get());
        userProfileManager.activateStatus(auth.get().getId());
        return true;
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> auth = authRepository.findByUsername(dto.getUsername());
        if (auth.isEmpty() || !passwordEncoder.matches(dto.getPassword(), auth.get().getPassword())) {
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        } else if (auth.get().getStatus().equals(EStatus.PENDING))
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        return jwtTokenProvider.createToken(auth.get().getId(), auth.get().getRole())
                .orElseThrow(() -> {
                    throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                });
    }

    public String loginMD5(LoginRequestDto dto) {
        String newPass = MD5Encoding.md5(dto.getPassword());
        Optional<Auth> auth = authRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        return jwtTokenProvider.createToken(auth.get().getId(), auth.get().getRole())
                .orElseThrow(() -> {
                    throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                });
    }

    public Boolean updateAuth(UpdateEmailOrUsernameRequestDto dto) {
        try {
            Optional<Auth> auth = authRepository.findById(dto.getAuthId());
            if (auth.isEmpty())
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            /* user-service de UserProfileService de UserProfile parametreli çözüm için
            auth.get().setEmail(dto.getEmail());
            auth.get().setUsername(dto.getUsername());
             */
            IAuthMapper.INSTANCE.updateUsernameOrEmail(dto, auth.get());
            update(auth.get());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Beklenmeyen hata");
        }
    }

    public Boolean delete(String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }

        Optional<Auth> auth = authRepository.findById(authId.get());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }

        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userProfileManager.delete(authId.get());
        return true;
    }

    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        if (dto.getPassword().equals(dto.getRepassword())) {
            auth.setActivationCode(CodeGenerator.generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            registerProducer.sendNewUser(IAuthMapper.INSTANCE.fromAuthToRegisterModel(auth));
            registerMailProducer.sendActivationCode(IAuthMapper.INSTANCE.fromAuthToRegisterMailModel(auth));
        } else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        return IAuthMapper.INSTANCE.toRegisterResponse(auth);
    }

    public List<Long> findByRole(String role) {
        ERole roles = ERole.valueOf(role.toUpperCase(Locale.ENGLISH));
        return authRepository.findByRole(roles).stream().map(x -> x.getId()).collect(Collectors.toList());
    }

    public Boolean changePassword(ToAuthChangePasswordDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        auth.get().setPassword(dto.getPassword());
        authRepository.save(auth.get());
        return true;
    }

    public Boolean forgotPassword(String email, String username) {
        Optional<Auth> auth = authRepository.findOptionalByEmail(email);
        if (auth.get().getStatus().equals(EStatus.ACTIVE)) {
            if (auth.get().getUsername().equals(username)) {
                //random password variable
                String randomPassword = UUID.randomUUID().toString();
                auth.get().setPassword(passwordEncoder.encode(randomPassword));
                save(auth.get());
                ForgotPasswordMailResponseDto dto = ForgotPasswordMailResponseDto.builder()
                        .password(randomPassword)
                        .email(email)
                        .build();
                eMailManager.forgotPasswordMail(dto);
                UserProfileChangePassword userProfileChangePassword = UserProfileChangePassword.builder()
                        .authId(auth.get().getId())
                        .password(auth.get().getPassword())
                        .build();
                userProfileManager.forgotPassword(userProfileChangePassword);
                return true;
            } else {
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            if (auth.get().getStatus().equals(EStatus.DELETED)) {
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            }
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }

    }
}
