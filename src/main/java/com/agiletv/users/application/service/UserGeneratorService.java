package com.agiletv.users.application.service;

import com.agiletv.users.application.exception.UserGeneratorServiceException;
import com.agiletv.users.application.service.dto.UserGeneratorDto;
import java.util.List;

public interface UserGeneratorService {
    List<UserGeneratorDto> generateUsers(Integer quantityUsersToGenerate) throws UserGeneratorServiceException;
}