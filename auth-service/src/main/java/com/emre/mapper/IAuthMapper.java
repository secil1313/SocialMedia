package com.emre.mapper;

import com.emre.dto.request.NewCreateUserRequestDto;
import com.emre.dto.request.RegisterRequestDto;
import com.emre.dto.request.ToAuthChangePasswordDto;
import com.emre.dto.request.UpdateEmailOrUsernameRequestDto;
import com.emre.dto.response.RegisterResponseDto;
import com.emre.rabbitmq.model.RegisterMailModel;
import com.emre.rabbitmq.model.RegisterModel;
import com.emre.repository.entity.Auth;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    Auth toAuth(final RegisterRequestDto dto);

    @Mapping(source = "id", target = "authId")
    RegisterModel fromAuthToRegisterModel(final Auth auth);
    RegisterMailModel fromAuthToRegisterMailModel(final Auth auth);

    RegisterResponseDto toRegisterResponse(final Auth auth);

    @Mapping(source = "id", target = "authId")
    NewCreateUserRequestDto fromAuthToNewCreateUserDto(final Auth auth);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUsernameOrEmail(UpdateEmailOrUsernameRequestDto dto, @MappingTarget Auth auth);



}

