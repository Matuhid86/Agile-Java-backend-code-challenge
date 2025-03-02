package com.agiletv.user.context.infrastructure.service;

import com.agiletv.user.app.exception.BadRequestException;
import com.agiletv.user.app.exception.NotFoundException;
import com.agiletv.user.context.infrastructure.domain.UserEntity;
import com.agiletv.user.context.mapper.UserMapper;
import com.agiletv.user.context.repository.UserRepository;
import com.agiletv.user.context.model.PagedResultDto;
import com.agiletv.user.context.model.UserDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericService<UserDto, UserEntity, String> {

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
    }

    @Override
    public List<UserDto> getAll() {
        return super.getAll();
    }

    public PagedResultDto<UserDto> getAll(Integer page, Integer size, String sortBy, String direction)
            throws BadRequestException {
        Page<UserDto> resultPage;
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            if (page <= 0) {
                throw new BadRequestException("The page must not be less than 1");
            }
            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();
            resultPage = super.getAll(PageRequest.of(page - 1, size, sort));
        } else {
            resultPage = new PageImpl<>(super.getAll());
        }
        return PagedResultDto.<UserDto>builder()
                .content(resultPage.getContent()).page(resultPage.getNumber() + 1)
                .size(resultPage.getSize()).totalPages(resultPage.getTotalPages())
                .totalElements(resultPage.getTotalElements())
                .build();
    }

    public UserDto getByUsername(String username) throws NotFoundException {
        return super.getById(username).orElseThrow(() ->
                new NotFoundException(String.format("The username %s not exist", username)));
    }

    public UserDto create(UserDto user) throws BadRequestException {
        if (repository.existsById(user.getUsername())) {
            throw new BadRequestException(String.format("The username %s already exist", user.getUsername()));
        }
        return super.save(user);
    }

    @Override
    public UserDto update(String username, UserDto user) throws NotFoundException {
        try { return super.update(username, user); }
        catch (NotFoundException exception) {
            throw new NotFoundException(String.format("The username %s not exist", username));
        }
    }

    @Override
    public void delete(String username) throws NotFoundException {
        try { super.delete(username); }
        catch (NotFoundException exception) {
            throw new NotFoundException(String.format("The username %s not exist", username));
        }
    }

    public Map<String, Object> getTree() {
        Map<String, Object> tree = new HashMap<>();

        this.getAll().forEach(user -> {
            tree.computeIfAbsent(user.getLocation().getCountry(), k -> new HashMap<String, Object>());
            Map<String, Object> countryMap = (Map<String, Object>) tree.get(user.getLocation().getCountry());

            countryMap.computeIfAbsent(user.getLocation().getState(), k -> new HashMap<String, Object>());
            Map<String, Object> stateMap = (Map<String, Object>) countryMap.get(user.getLocation().getState());

            stateMap.computeIfAbsent(user.getLocation().getCity(), k -> new ArrayList<>());
            List<UserDto> cityUsers = (List<UserDto>) stateMap.get(user.getLocation().getCity());

            cityUsers.add(user);
        });

        return tree;
    }
}
