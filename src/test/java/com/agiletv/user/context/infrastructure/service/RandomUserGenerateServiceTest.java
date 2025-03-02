package com.agiletv.user.context.infrastructure.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.agiletv.user.app.config.TestConfig;
import com.agiletv.user.context.infrastructure.exception.UserGeneratorServiceException;
import com.agiletv.user.context.model.UserDto;
import com.agiletv.user.context.service.UserGeneratorService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class RandomUserGenerateServiceTest {

    @Autowired
    private UserGeneratorService service;

    @Test
    void generateUsersTest() throws UserGeneratorServiceException {
        Integer quantityUsersToGenerate = 2;
        List<UserDto> users = service.generateUsers(quantityUsersToGenerate);

        assertThat(users).hasSize(quantityUsersToGenerate);
        users.forEach(user -> {
            assertNotNull(user.getUsername());
            assertNotNull(user.getEmail());
            assertNotNull(user.getName());
            assertNotNull(user.getGender());
            assertNotNull(user.getUrlPhoto());
            assertNotNull(user.getLocation());
            assertNotNull(user.getLocation().getCity());
            assertNotNull(user.getLocation().getCountry());
            assertNotNull(user.getLocation().getState());
        });
    }
}