package com.emre.mapper;

import com.emre.dto.request.*;
import com.emre.rabbitmq.model.RegisterElasticModel;
import com.emre.rabbitmq.model.RegisterModel;
import com.emre.repository.entity.Follow;
import com.emre.repository.entity.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserProfileMapper {
    IUserProfileMapper INSTANCE = Mappers.getMapper(IUserProfileMapper.class);

    UserProfile updateFromDtoToUser(final NewCreateUserRequestDto dto);
    UserProfile fromRegisterModelToUserProfile(final RegisterModel model);

    RegisterElasticModel fromUserProfileToElasticModel(final UserProfile userProfile);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfile updateFromDtoToUser(UserProfileUpdateRequestDto dto, @MappingTarget UserProfile userProfile);

    //UpdateEmailOrUsernameRequestDto toUpdateUsernameOrEmail(final UserProfile userProfile);
    UpdateEmailOrUsernameRequestDto toUpdateUsernameOrEmail(final UserProfileUpdateRequestDto dto);

    Follow fromCreateFollowDtoToFollow(final String followId, final String userId);

    ToAuthChangePasswordDto fromUserProfileToAuthPasswordChangeDto(final UserProfile userProfile);

}
