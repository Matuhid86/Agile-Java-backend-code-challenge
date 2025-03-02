package com.agiletv.user.context.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.agiletv.user.app.config.TestConfig;
import com.agiletv.user.context.infrastructure.domain.UserEntity;
import com.agiletv.user.context.mapper.UserMapper;
import com.agiletv.user.context.model.UserDto;
import java.util.ArrayList;
import java.util.List;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void dtoToEntityTest() {
        UserDto userDto = new EasyRandom().nextObject(UserDto.class);

        UserEntity userEntity = mapper.toEntity(userDto);

        assertNotNull(userEntity);
        assertEquals(userDto.getUsername(), userEntity.getUsername());
        assertEquals(userDto.getEmail(), userEntity.getEmail());
        assertEquals(userDto.getName(), userEntity.getName());
        assertEquals(userDto.getGender(), userEntity.getGender());
        assertEquals(userDto.getUrlPhoto(), userEntity.getUrlPhoto());
        assertNotNull(userEntity.getLocation());
        assertEquals(userDto.getLocation().getCity(), userEntity.getLocation().getCity());
        assertEquals(userDto.getLocation().getCountry(), userEntity.getLocation().getCountry());
        assertEquals(userDto.getLocation().getState(), userEntity.getLocation().getState());
    }

    @Test
    void entityToDtoTest() {
        UserEntity userEntity = new EasyRandom().nextObject(UserEntity.class);

        UserDto userDto = mapper.toDto(userEntity);

        assertNotNull(userDto);
        assertEquals(userEntity.getUsername(), userDto.getUsername());
        assertEquals(userEntity.getEmail(), userDto.getEmail());
        assertEquals(userEntity.getName(), userDto.getName());
        assertEquals(userEntity.getGender(), userDto.getGender());
        assertEquals(userEntity.getUrlPhoto(), userDto.getUrlPhoto());
        assertNotNull(userDto.getLocation());
        assertEquals(userEntity.getLocation().getCity(), userDto.getLocation().getCity());
        assertEquals(userEntity.getLocation().getCountry(), userDto.getLocation().getCountry());
        assertEquals(userEntity.getLocation().getState(), userDto.getLocation().getState());
    }

    @Test
    void dtosToEntitiesTest() {
        List<UserDto> userDtos = new ArrayList<>();
        UserDto userDto = new EasyRandom().nextObject(UserDto.class);

        userDtos.add(userDto);

        List<UserEntity> userEntities = mapper.toEntities(userDtos);

        assertNotNull(userEntities);
        assertThat(userEntities).hasSize(1);
        assertEquals(userDto.getUsername(), userEntities.get(0).getUsername());
        assertEquals(userDto.getEmail(), userEntities.get(0).getEmail());
        assertEquals(userDto.getName(), userEntities.get(0).getName());
        assertEquals(userDto.getGender(), userEntities.get(0).getGender());
        assertEquals(userDto.getUrlPhoto(), userEntities.get(0).getUrlPhoto());
        assertNotNull(userEntities.get(0).getLocation());
        assertEquals(userDto.getLocation().getCity(), userEntities.get(0).getLocation().getCity());
        assertEquals(userDto.getLocation().getCountry(), userEntities.get(0).getLocation().getCountry());
        assertEquals(userDto.getLocation().getState(), userEntities.get(0).getLocation().getState());
    }

    @Test
    void entitiesToDtosTest() {
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity = new EasyRandom().nextObject(UserEntity.class);

        userEntities.add(userEntity);

        List<UserDto> userDtos = mapper.toDtos(userEntities);

        assertNotNull(userDtos);
        assertThat(userDtos).hasSize(1);
        assertEquals(userEntity.getUsername(), userDtos.get(0).getUsername());
        assertEquals(userEntity.getEmail(), userDtos.get(0).getEmail());
        assertEquals(userEntity.getName(), userDtos.get(0).getName());
        assertEquals(userEntity.getGender(), userDtos.get(0).getGender());
        assertEquals(userEntity.getUrlPhoto(), userDtos.get(0).getUrlPhoto());
        assertNotNull(userDtos.get(0).getLocation());
        assertEquals(userEntity.getLocation().getCity(), userDtos.get(0).getLocation().getCity());
        assertEquals(userEntity.getLocation().getCountry(), userDtos.get(0).getLocation().getCountry());
        assertEquals(userEntity.getLocation().getState(), userDtos.get(0).getLocation().getState());
    }

    @Test
    void updateEntityFromDtoTest() {
        UserDto userDto = new EasyRandom().nextObject(UserDto.class);
        UserEntity userEntity = new EasyRandom().nextObject(UserEntity.class);

        mapper.updateEntityFromDto(userDto, userEntity);

        assertEquals(userDto.getUsername(), userEntity.getUsername());
        assertEquals(userDto.getEmail(), userEntity.getEmail());
        assertEquals(userDto.getName(), userEntity.getName());
        assertEquals(userDto.getGender(), userEntity.getGender());
        assertEquals(userDto.getUrlPhoto(), userEntity.getUrlPhoto());
        assertNotNull(userEntity.getLocation());
        assertEquals(userDto.getLocation().getCity(), userEntity.getLocation().getCity());
        assertEquals(userDto.getLocation().getCountry(), userEntity.getLocation().getCountry());
        assertEquals(userDto.getLocation().getState(), userEntity.getLocation().getState());
    }
}
