package com.agiletv.users.infrastructure.persistence.mapper;

import com.agiletv.users.domain.model.IUser;
import com.agiletv.users.domain.model.User;
import com.agiletv.users.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    User toDomain(UserEntity user);

    User toDomain(IUser user);

    UserEntity toEntity(User user);

    UserEntity toEntity(IUser user);
}