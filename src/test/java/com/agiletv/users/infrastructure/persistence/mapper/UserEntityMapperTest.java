package com.agiletv.users.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.agiletv.users.application.config.TestConfig;
import com.agiletv.users.domain.model.User;
import com.agiletv.users.infrastructure.persistence.entity.UserEntity;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class UserEntityMapperTest {

    @Autowired
    private UserEntityMapper mapper;

    @Test
    void domainToEntityTest() {
        User user = new EasyRandom().nextObject(User.class);

        UserEntity userEntity = mapper.toEntity(user);

        assertNotNull(userEntity);
        assertEquals(user.getUsername(), userEntity.getUsername());
        assertEquals(user.getEmail(), userEntity.getEmail());
        assertEquals(user.getName(), userEntity.getName());
        assertEquals(user.getGender(), userEntity.getGender());
        assertEquals(user.getUrlPhoto(), userEntity.getUrlPhoto());
        assertNotNull(userEntity.getLocation());
        assertEquals(user.getLocation().getCity(), userEntity.getLocation().getCity());
        assertEquals(user.getLocation().getCountry(), userEntity.getLocation().getCountry());
        assertEquals(user.getLocation().getState(), userEntity.getLocation().getState());
    }

    @Test
    void entityToDomainTest() {
        UserEntity userEntity = new EasyRandom().nextObject(UserEntity.class);

        User user = mapper.toDomain(userEntity);

        assertNotNull(user);
        assertEquals(userEntity.getUsername(), user.getUsername());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getName(), user.getName());
        assertEquals(userEntity.getGender(), user.getGender());
        assertEquals(userEntity.getUrlPhoto(), user.getUrlPhoto());
        assertNotNull(user.getLocation());
        assertEquals(userEntity.getLocation().getCity(), user.getLocation().getCity());
        assertEquals(userEntity.getLocation().getCountry(), user.getLocation().getCountry());
        assertEquals(userEntity.getLocation().getState(), user.getLocation().getState());
    }
}
