package com.agiletv.user.context.service;

import com.agiletv.user.context.infrastructure.exception.UserGeneratorServiceException;
import com.agiletv.user.context.model.UserDto;
import java.util.List;

public interface UserGeneratorService {
    List<UserDto> generateUsers(Integer quantityUsersToGenerate) throws UserGeneratorServiceException;
}