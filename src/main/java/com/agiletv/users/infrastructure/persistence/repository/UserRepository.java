package com.agiletv.users.infrastructure.persistence.repository;

import com.agiletv.users.domain.model.IUser;
import com.agiletv.users.domain.model.PagedResult;
import com.agiletv.users.domain.repository.IUserRepository;
import com.agiletv.users.infrastructure.persistence.entity.UserEntity;
import com.agiletv.users.infrastructure.persistence.mapper.UserEntityMapper;
import com.agiletv.users.infrastructure.persistence.repository.jpa.UserRepositoryJpa;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements IUserRepository {
    private final UserRepositoryJpa repository;
    private final UserEntityMapper mapper;

    @Autowired
    public UserRepository(UserRepositoryJpa repository, UserEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public IUser save(IUser user) {
        return mapper.toDomain(repository.save(mapper.toEntity(user)));
    }

    @Override
    public Optional<IUser> findByUsername(String username) {
        return repository.findById(username).map(mapper::toDomain);
    }

    @Override
    public Boolean existByUsername(String username) {
        return repository.existsById(username);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.deleteById(username);
    }

    @Override
    public List<IUser> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public PagedResult<IUser> findAll(Integer pageNumber, Integer pageSize) {
        Page<UserEntity> userEntities = repository.findAll(PageRequest.of(pageNumber - 1, pageSize));

        return PagedResult.<IUser>builder()
                          .size(userEntities.getSize())
                          .page(userEntities.getNumber() + 1)
                          .content(userEntities.getContent().stream()
                                               .map(mapper::toDomain)
                                               .collect(Collectors.toList()))
                          .totalPages(userEntities.getTotalPages())
                          .totalElements(userEntities.getTotalElements())
                          .build();
    }
}