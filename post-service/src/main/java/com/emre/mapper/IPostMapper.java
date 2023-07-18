package com.emre.mapper;

import com.emre.dto.request.CreateNewPostRequestDto;
import com.emre.dto.request.UpdatePostRequestDto;
import com.emre.dto.response.UserProfileResponseDto;
import com.emre.repository.entity.Like;
import com.emre.repository.entity.Post;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPostMapper {
    IPostMapper INSTANCE = Mappers.getMapper(IPostMapper.class);
    Post toPost(final CreateNewPostRequestDto dto);

}
