package com.agiletv.user.context.infrastructure.repository;

import com.agiletv.user.context.infrastructure.domain.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<UserEntity, String> { }