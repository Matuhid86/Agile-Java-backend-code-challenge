package com.agiletv.users.domain.repository;

import com.agiletv.users.domain.model.IUser;
import com.agiletv.users.domain.model.PagedResult;
import java.util.List;
import java.util.Optional;


public interface IUserRepository {
    IUser save(IUser user);

    Optional<IUser> findByUsername(String username);

    Boolean existByUsername(String username);

    void deleteByUsername(String username);

    List<IUser> findAll();

    PagedResult<IUser> findAll(Integer pageNumber, Integer pageSize);
}