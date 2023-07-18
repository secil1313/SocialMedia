package com.emre.service;

import com.emre.dto.request.ChangePasswordRequestDto;
import com.emre.dto.request.NewCreateUserRequestDto;
import com.emre.dto.request.UpdateEmailOrUsernameRequestDto;
import com.emre.dto.request.UserProfileUpdateRequestDto;
import com.emre.dto.response.UserProfileChangePasswordResponseDto;
import com.emre.exception.ErrorType;
import com.emre.exception.UserProfileManagerException;
import com.emre.manager.IAuthManager;
import com.emre.mapper.IUserProfileMapper;
import com.emre.rabbitmq.model.RegisterModel;
import com.emre.rabbitmq.producer.RegisterElasticProducer;
import com.emre.repository.IUserProfileRepository;
import com.emre.repository.entity.UserProfile;
import com.emre.repository.enums.EStatus;
import com.emre.utility.JwtTokenProvider;
import com.emre.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    private final IAuthManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CacheManager cacheManager; //Cache'i silebilmek için gibi düşünülebilir.
    private final PasswordEncoder passwordEncoder;
    private final RegisterElasticProducer registerElasticProducer;

    public UserProfileService(IUserProfileRepository userProfileRepository,
                              IAuthManager authManager,
                              JwtTokenProvider jwtTokenProvider,
                              CacheManager cacheManager,
                              PasswordEncoder passwordEncoder, RegisterElasticProducer registerElasticProducer) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cacheManager = cacheManager;
        this.passwordEncoder = passwordEncoder;
        this.registerElasticProducer = registerElasticProducer;
    }

    @CacheEvict(value = "find-by-role", allEntries = true)
    public Boolean createUser(String token,NewCreateUserRequestDto dto) {
        try {

            UserProfile userProfile = userProfileRepository.save(IUserProfileMapper.INSTANCE.updateFromDtoToUser(dto));
            cacheManager.getCache("findAll").clear(); //evict ile yapmadık çünkü key yok. key olması için metotda param. vermemiz gerekiyor.
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Beklenmeyen bir durum oluştu.");
        }
    }

    @Cacheable(value = "findAll")
    public List<UserProfile> findAll() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userProfileRepository.findAll();
    }

    @CacheEvict(value = "find-by-username", key = "#model.username.toLowerCase()")
    public Boolean createUserWithRabbitMq(RegisterModel model) {
        try {
            UserProfile userProfile = save(IUserProfileMapper.INSTANCE.fromRegisterModelToUserProfile(model));
            registerElasticProducer.sendNewUser(IUserProfileMapper.INSTANCE.fromUserProfileToElasticModel(userProfile));
            cacheManager.getCache("findAll").clear();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Beklenmeyen bir durum oluştu.");
        }
    }

    public Boolean activateStatus(Long authId) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty())
            throw new RuntimeException("Auth id bulunamadı");
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }

    //cache delete yerine cache i update etmeye yaramaktadır.
    @CachePut(value = "find-by-username", key = "#dto.username.toLowerCase()")
    //unless -> belli şartlara göre cachelenmesini sağlayan özellik.
    public UserProfile update(UserProfileUpdateRequestDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        //cache delete
        //cacheManager.getCache("find-by-username").evict(userProfile.get().getUsername().toLowerCase());

        /*
        ----UserProfile Çözümü(Cenk)----
        update(IUserProfileMapper.INSTANCE.updateFromDtoToUserProfile(dto, userProfile.get()));
        authManager.updateUsernameOrEmail(IUserProfileMapper.INSTANCE.toUpdateUsernameEmail(userProfile.get()));
        */
        UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto = IUserProfileMapper.INSTANCE.toUpdateUsernameOrEmail(dto);
        updateEmailOrUsernameRequestDto.setAuthId(authId.get());
        authManager.updateEmailOrUsername(updateEmailOrUsernameRequestDto);
        update(IUserProfileMapper.INSTANCE.updateFromDtoToUser(dto, userProfile.get()));

        //Bertan Çözüm
        //cacheManager.getCache("find-by-username").put(userProfile.get().getUsername().toString().toLowerCase(), userProfile.get());
        return userProfile.get();
    }

    public Boolean delete(Long authId) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }

    /**
     * İçerisinde ki key ile birlikte farklı yazım tarzları da olsa aynı şeyi yazdığımız da
     * tekrar tekrar cache leme işlemini engellemiş oluyoruz.
     * toLowerCase yaparak her şeyi o yöne(küçülterek) çeviriyor.
     */
    @Cacheable(value = "find-by-username", key = "#username.toLowerCase()")
    public UserProfile findByUsername(String username) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByUsernameIgnoreCase(username);
        try {
            Thread.sleep(1000);
            if (userProfile.isEmpty())
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            return userProfile.get();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Cacheable(value = "find-by-role", key = "#role.toUpperCase()") //USER, ADMIN
    public List<UserProfile> findByRole(String role) {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //auth manager
        List<Long> authIds = authManager.findByRole(role).getBody(); //getBody() ile responseentitiy den kurtardık.
        return authIds.stream().map(
                        x -> userProfileRepository.findOptionalByAuthId(x)
                                .orElseThrow(() -> { //orElseThrow ->bu bir optinal metodu. çalışırsa çalış yoksa hata fırlat.
                                    throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
                                }))
                .collect(Collectors.toList());
    }

    //FollowService için oluşturuldu. FollowService için Controllerda yazmadık.
    //PostService oluşturulduktan sonra post(openfeign) için de kullanıldı. Ama bu sefer controllerda kullanıldı.
    public Optional<UserProfile> findByAuthId(Long authId) {
        return userProfileRepository.findOptionalByAuthId(authId);
    }

    @CacheEvict(value = "find-by-username", allEntries = true)
    public Boolean changePassword(ChangePasswordRequestDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (!passwordEncoder.matches(dto.getOldPassword(), userProfile.get().getPassword())) {
            throw new RuntimeException("Eski şifrenizi doğru girmediniz.");
        }
        userProfile.get().setPassword(passwordEncoder.encode(dto.getNewPassword()));
        cacheManager.getCache("findAll").clear();
        userProfileRepository.save(userProfile.get());
        authManager.changePassword(IUserProfileMapper.INSTANCE.fromUserProfileToAuthPasswordChangeDto(userProfile.get()));
        return true;
    }

    public Boolean forgotPassword(UserProfileChangePasswordResponseDto dto) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (userProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        userProfile.get().setPassword(dto.getPassword());
        update(userProfile.get());
        return true;
    }

}
