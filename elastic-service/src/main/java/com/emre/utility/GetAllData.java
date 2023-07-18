package com.emre.utility;

import com.emre.manager.IUserManager;
import com.emre.repository.entity.UserProfile;
import com.emre.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllData {
    private final IUserManager userManager;
    private final UserProfileService userProfileService;

    //@PostConstruct //--> bir kere çalışması gerekmektedir, aksi halde veri tekrarı yaşanabilir
    public void initData(){
        List<UserProfile> userProfiles = userManager.findAll().getBody();
        userProfileService.saveAll(userProfiles);
    }
}
