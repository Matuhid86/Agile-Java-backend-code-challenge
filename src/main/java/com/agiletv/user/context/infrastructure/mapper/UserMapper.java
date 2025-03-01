package com.agiletv.user.context.infrastructure.mapper;

import com.agiletv.user.context.infrastructure.domain.UserEntity;
import com.agiletv.user.context.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = GenericMapper.class)
public interface UserMapper extends GenericMapper<UserDto, UserEntity>  { }