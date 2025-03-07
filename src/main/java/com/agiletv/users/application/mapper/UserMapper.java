package com.agiletv.users.application.mapper;

import com.agiletv.users.application.dto.UserDto;
import com.agiletv.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper  {
    UserDto toDto(User user);

    User toDomain(UserDto user);

    void updateDomainFromDto(UserDto dto, @MappingTarget User domain);
}