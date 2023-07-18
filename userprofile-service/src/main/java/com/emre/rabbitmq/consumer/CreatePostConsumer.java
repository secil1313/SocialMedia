package com.emre.rabbitmq.consumer;

import com.emre.rabbitmq.model.CreatePostModel;
import com.emre.rabbitmq.model.UserProfileResponseModel;
import com.emre.repository.entity.UserProfile;
import com.emre.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatePostConsumer {
    private final UserProfileService userProfileService;

    @RabbitListener(queues = ("${rabbitmq.queueRegister}"))
    public Object createPost(CreatePostModel model) {
        Optional<UserProfile> userProfile = userProfileService.findByAuthId(model.getAuthId());
        UserProfileResponseModel userProfileResponseModel = UserProfileResponseModel.builder()
                .userId(userProfile.get().getId())
                .username(userProfile.get().getUsername())
                .avatar(userProfile.get().getAvatar())
                .build();
        return userProfileResponseModel;
    }

}
