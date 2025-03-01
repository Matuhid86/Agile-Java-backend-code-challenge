package com.agiletv.user.context.infrastructure.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiletv.user.app.exception.BadRequestException;
import com.agiletv.user.app.exception.NotFoundException;
import com.agiletv.user.context.infrastructure.domain.LocationEntity;
import com.agiletv.user.context.infrastructure.domain.UserEntity;
import com.agiletv.user.context.infrastructure.mapper.UserMapper;
import com.agiletv.user.context.infrastructure.repository.UserRepository;
import com.agiletv.user.context.model.dto.LocationDto;
import com.agiletv.user.context.model.dto.PagedResultDto;
import com.agiletv.user.context.model.dto.UserDto;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void getAllTest() {
        UserEntity entity1 = new EasyRandom().nextObject(UserEntity.class);
        UserEntity entity2 = new EasyRandom().nextObject(UserEntity.class);
        UserDto dto1 = UserDto.builder()
                              .urlPhoto(entity1.getUrlPhoto())
                              .name(entity1.getName())
                              .username(entity1.getUsername())
                              .email(entity1.getEmail())
                              .gender(entity1.getGender())
                              .location(Objects.nonNull(entity1.getLocation()) ? LocationDto.builder()
                                                                                            .state(entity1.getLocation().getState())
                                                                                            .country(entity1.getLocation().getCountry())
                                                                                            .city(entity1.getLocation().getCity())
                                                                                            .build() : null)
                              .build();
        UserDto dto2 = UserDto.builder()
                              .urlPhoto(entity2.getUrlPhoto())
                              .name(entity2.getName())
                              .username(entity2.getUsername())
                              .email(entity2.getEmail())
                              .gender(entity2.getGender())
                              .location(Objects.nonNull(entity2.getLocation()) ? LocationDto.builder()
                                                                                            .state(entity2.getLocation().getState())
                                                                                            .country(entity2.getLocation().getCountry())
                                                                                            .city(entity2.getLocation().getCity())
                                                                                            .build() : null)
                              .build();

        List<UserEntity> entities = List.of(entity1, entity2);
        List<UserDto> dtos = List.of(dto1, dto2);

        when(userRepository.findAll()).thenReturn(entities);
        when(userMapper.toDtos(entities)).thenReturn(dtos);

        List<UserDto> result = userService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDtos(any());
    }

    @ParameterizedTest
    @MethodSource("getAllPaginationCases")
    void getAllPaginationTest(Integer page, Integer size, String sortBy, String direction, Boolean isValid,
                                    String expectedMessage) {
        UserEntity entity1 = new EasyRandom().nextObject(UserEntity.class);
        UserEntity entity2 = new EasyRandom().nextObject(UserEntity.class);
        UserDto dto1 = UserDto.builder()
                              .urlPhoto(entity1.getUrlPhoto())
                              .name(entity1.getName())
                              .username(entity1.getUsername())
                              .email(entity1.getEmail())
                              .gender(entity1.getGender())
                              .location(Objects.nonNull(entity1.getLocation()) ? LocationDto.builder()
                                                                                            .state(entity1.getLocation().getState())
                                                                                            .country(entity1.getLocation().getCountry())
                                                                                            .city(entity1.getLocation().getCity())
                                                                                            .build() : null)
                              .build();
        UserDto dto2 = UserDto.builder()
                              .urlPhoto(entity2.getUrlPhoto())
                              .name(entity2.getName())
                              .username(entity2.getUsername())
                              .email(entity2.getEmail())
                              .gender(entity2.getGender())
                              .location(Objects.nonNull(entity2.getLocation()) ? LocationDto.builder()
                                                                                            .state(entity2.getLocation().getState())
                                                                                            .country(entity2.getLocation().getCountry())
                                                                                            .city(entity2.getLocation().getCity())
                                                                                            .build() : null)
                              .build();

        List<UserEntity> entities = List.of(entity1, entity2);
        List<UserDto> dtos = List.of(dto1, dto2);

        if (isValid) {
            when(userMapper.toDtos(entities)).thenReturn(dtos);

            if (Objects.isNull(page) || Objects.isNull(size)) {
                when(userRepository.findAll()).thenReturn(entities);

                PagedResultDto<UserDto> result = assertDoesNotThrow(() -> userService.getAll(page, size, sortBy, direction));

                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                verify(userRepository, times(1)).findAll();

            } else {
                Page<UserEntity> userPage = new PageImpl<>(entities,
                        PageRequest.of(page - 1, size, Sort.by(sortBy).ascending()),
                        entities.size()
                );

                when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

                PagedResultDto<UserDto> result = assertDoesNotThrow(
                        () -> userService.getAll(page, size, sortBy, direction));

                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(page, result.getPage());
                assertEquals(size, result.getSize());

                verify(userRepository, times(1)).findAll(any(Pageable.class));
                verify(userMapper, times(1)).toDtos(any());
            }
        } else {
            BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.getAll(page, size, sortBy, direction));
            assertEquals(expectedMessage, exception.getMessage());

            verify(userRepository, times(0)).findAll(any(Pageable.class));
            verify(userMapper, times(0)).toDtos(any());
        }
    }

    @ParameterizedTest
    @MethodSource("getByUsernameCases")
    void getByUsernameTest(String username, Boolean isValid, String expectedMessage) {
        UserEntity entity = new EasyRandom().nextObject(UserEntity.class);
        entity.setUsername("username_test");

        UserDto expectedDto = UserDto.builder()
                              .urlPhoto(entity.getUrlPhoto())
                              .name(entity.getName())
                              .username(entity.getUsername())
                              .email(entity.getEmail())
                              .gender(entity.getGender())
                              .location(Objects.nonNull(entity.getLocation()) ? LocationDto.builder()
                                                                                            .state(entity.getLocation().getState())
                                                                                            .country(entity.getLocation().getCountry())
                                                                                            .city(entity.getLocation().getCity())
                                                                                            .build() : null)
                              .build();

        if (isValid) {
            when(userMapper.toDto(entity)).thenReturn(expectedDto);
            when(userRepository.findById(username)).thenReturn(Optional.of(entity));

            UserDto dto = assertDoesNotThrow(() -> userService.getByUsername(username));

            assertNotNull(dto);
            assertEquals(username, dto.getUsername());

            verify(userRepository, times(1)).findById(any());
            verify(userMapper, times(1)).toDto(any());
        } else {
            NotFoundException exception = assertThrows(
                    NotFoundException.class, () -> userService.getByUsername(username));
            assertEquals(expectedMessage, exception.getMessage());

            verify(userRepository, times(1)).findById(any());
            verify(userMapper, times(0)).toDto(any());
        }
    }

    @ParameterizedTest
    @MethodSource("createCases")
    void createTest(String username, Boolean existUser, String expectedMessage) {
        UserEntity entity = new EasyRandom().nextObject(UserEntity.class);
        entity.setUsername(username);

        UserDto dto = UserDto.builder()
                                     .urlPhoto(entity.getUrlPhoto())
                                     .name(entity.getName())
                                     .username(entity.getUsername())
                                     .email(entity.getEmail())
                                     .gender(entity.getGender())
                                     .location(Objects.nonNull(entity.getLocation()) ? LocationDto.builder()
                                                                                                  .state(entity.getLocation().getState())
                                                                                                  .country(entity.getLocation().getCountry())
                                                                                                  .city(entity.getLocation().getCity())
                                                                                                  .build() : null)
                                     .build();

        when(userRepository.existsById(any())).thenReturn(existUser);

        if (!existUser) {
            when(userMapper.toDto(entity)).thenReturn(dto);
            when(userMapper.toEntity(dto)).thenReturn(entity);
            when(userRepository.save(entity)).thenReturn(entity);

            UserDto dtoCreated = assertDoesNotThrow(() -> userService.create(dto));

            assertNotNull(dtoCreated);
            assertEquals(dto.getUsername(), dtoCreated.getUsername());
            assertEquals(dto.getEmail(), dtoCreated.getEmail());
            assertEquals(dto.getName(), dtoCreated.getName());
            assertEquals(dto.getUrlPhoto(), dtoCreated.getUrlPhoto());
            assertEquals(dto.getGender(), dtoCreated.getGender());
            assertEquals(dto.getLocation().getCity(), dtoCreated.getLocation().getCity());
            assertEquals(dto.getLocation().getCountry(), dtoCreated.getLocation().getCountry());
            assertEquals(dto.getLocation().getState(), dtoCreated.getLocation().getState());

            verify(userRepository, times(1)).save(any());
            verify(userMapper, times(1)).toDto(any());
            verify(userMapper, times(1)).toEntity(any());
        } else {
            BadRequestException exception = assertThrows(
                    BadRequestException.class, () -> userService.create(dto));
            assertEquals(expectedMessage, exception.getMessage());

            verify(userRepository, times(0)).save(any());
            verify(userMapper, times(0)).toDto(any());
            verify(userMapper, times(0)).toEntity(any());
        }

        verify(userRepository, times(1)).existsById(any());
    }

    @ParameterizedTest
    @MethodSource("updateCases")
    void updateTest(String username, Boolean existUser, String expectedMessage) {
        UserEntity entity = new EasyRandom().nextObject(UserEntity.class);
        entity.setUsername(username);

        UserDto dto = new EasyRandom().nextObject(UserDto.class);
        dto.setUsername(username);

        LocationEntity locationEntitySaved = new LocationEntity();
        locationEntitySaved.setCity(dto.getLocation().getCity());
        locationEntitySaved.setState(dto.getLocation().getState());
        locationEntitySaved.setCountry(dto.getLocation().getCountry());

        UserEntity entitySaved = new UserEntity();
        entitySaved.setUsername(username);
        entitySaved.setName(dto.getName());
        entitySaved.setGender(dto.getGender());
        entitySaved.setEmail(dto.getEmail());
        entitySaved.setUrlPhoto(dto.getUrlPhoto());
        entitySaved.setLocation(locationEntitySaved);

        when(userRepository.findById(username)).thenReturn(existUser ? Optional.of(entity) : Optional.empty());

        if (existUser) {
            doNothing().when(userMapper).updateEntityFromDto(dto, entity);
            when(userMapper.toDto(entitySaved)).thenReturn(dto);
            when(userRepository.save(entity)).thenReturn(entitySaved);

            UserDto dtoUpdated = assertDoesNotThrow(() -> userService.update(username, dto));

            assertNotNull(dtoUpdated);
            assertEquals(dto.getUsername(), dtoUpdated.getUsername());
            assertEquals(dto.getEmail(), dtoUpdated.getEmail());
            assertEquals(dto.getName(), dtoUpdated.getName());
            assertEquals(dto.getUrlPhoto(), dtoUpdated.getUrlPhoto());
            assertEquals(dto.getGender(), dtoUpdated.getGender());
            assertEquals(dto.getLocation().getCity(), dtoUpdated.getLocation().getCity());
            assertEquals(dto.getLocation().getCountry(), dtoUpdated.getLocation().getCountry());
            assertEquals(dto.getLocation().getState(), dtoUpdated.getLocation().getState());

            verify(userRepository, times(1)).save(any());
            verify(userMapper, times(1)).toDto(any());
            verify(userMapper, times(1)).updateEntityFromDto(any(), any());
        } else {
            NotFoundException exception = assertThrows(
                    NotFoundException.class, () -> userService.update(username, dto));
            assertEquals(expectedMessage, exception.getMessage());

            verify(userRepository, times(0)).save(any());
            verify(userMapper, times(0)).toDto(any());
            verify(userMapper, times(0)).updateEntityFromDto(any(), any());
        }

        verify(userRepository, times(1)).findById(any());
    }

    @ParameterizedTest
    @MethodSource("deleteCases")
    void deleteTest(String username, Boolean existUser, String expectedMessage) {
        when(userRepository.existsById(any())).thenReturn(existUser);

        if (existUser) {
            doNothing().when(userRepository).deleteById(username);

            assertDoesNotThrow(() -> userService.delete(username));

            verify(userRepository, times(1)).deleteById(any());
        } else {
            NotFoundException exception = assertThrows(
                    NotFoundException.class, () -> userService.delete(username));
            assertEquals(expectedMessage, exception.getMessage());

            verify(userRepository, times(0)).deleteById(any());
        }

        verify(userRepository, times(1)).existsById(any());
    }

    @Test
    void getTreeTest() {
        LocationDto locationDto1 = LocationDto.builder()
                                            .city("Los Angeles")
                                            .country("USA")
                                            .state("California")
                                            .build();
        LocationDto locationDto2 = LocationDto.builder()
                                              .city("San Francisco")
                                              .country("USA")
                                              .state("California")
                                              .build();
        LocationDto locationDto3 = LocationDto.builder()
                                              .city("Toronto")
                                              .country("Canada")
                                              .state("Ontario")
                                              .build();
        UserDto user1 = new EasyRandom().nextObject(UserDto.class);
        user1.setLocation(locationDto1);
        UserDto user2 = new EasyRandom().nextObject(UserDto.class);
        user2.setLocation(locationDto2);
        UserDto user3 = new EasyRandom().nextObject(UserDto.class);
        user3.setLocation(locationDto3);

        List<UserDto> users = List.of(user1, user2, user3);
        when(userMapper.toDtos(any())).thenReturn(users);

        Map<String, Object> tree = userService.getTree();

        assertNotNull(tree);
        assertTrue(tree.containsKey("USA"));
        assertTrue(tree.containsKey("Canada"));
        assertEquals(2, ((Map) ((Map) tree.get("USA")).get("California")).size());
        assertEquals(1, ((Map) ((Map) tree.get("Canada")).get("Ontario")).size());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDtos(any());
    }

    static Stream<Arguments> getAllPaginationCases() {
        return Stream.of(
                Arguments.of(1, 2, "username", "asc", true, ""),
                Arguments.of(0, 2, "username", "asc", false, "The page must not be less than 1"),
                Arguments.of(null, null, "username", "asc", true, "")
        );
    }

    static Stream<Arguments> getByUsernameCases() {
        return Stream.of(
                Arguments.of("username_test", true, ""),
                Arguments.of("not_found", false, "The username not_found not exist"));
    }

    static Stream<Arguments> createCases() {
        return Stream.of(
                Arguments.of("username_not_exist", false, ""),
                Arguments.of("username_exist", true, "The username username_exist already exist"));
    }

    static Stream<Arguments> updateCases() {
        return Stream.of(
                Arguments.of("username_exist", true, ""),
                Arguments.of("username_not_exist", false, "The username username_not_exist not exist"));
    }

    static Stream<Arguments> deleteCases() {
        return Stream.of(
                Arguments.of("username_exist", true, ""),
                Arguments.of("username_not_exist", false, "The username username_not_exist not exist"));
    }
}