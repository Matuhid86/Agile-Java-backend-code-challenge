package com.agiletv.users.application.service;

import com.agiletv.users.application.dto.LocationTreeDto;
import com.agiletv.users.application.exception.InternalErrorException;
import com.agiletv.users.application.exception.NotFoundException;
import com.agiletv.users.application.dto.UserDto;
import com.agiletv.users.domain.model.PagedResult;
import com.agiletv.users.web.exception.BadRequestException;
import java.util.List;

public interface IUserService {
    UserDto create(UserDto user) throws BadRequestException;
    UserDto getByUsername(String username) throws NotFoundException;
    PagedResult<UserDto> getAll(Integer pageNumber, Integer pageSize) throws BadRequestException;
    UserDto update(String username, UserDto user) throws NotFoundException;
    void delete(String username) throws NotFoundException;
    List<UserDto> generate(Integer quantityUsersToGenerate) throws BadRequestException, InternalErrorException;
    LocationTreeDto getTreeByLocation();
}
