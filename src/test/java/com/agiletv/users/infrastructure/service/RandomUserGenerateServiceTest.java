package com.agiletv.users.infrastructure.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.agiletv.users.application.config.TestConfig;
import com.agiletv.users.application.exception.UserGeneratorServiceException;
import com.agiletv.users.application.service.RandomUserGenerateService;
import com.agiletv.users.application.service.UserGeneratorService;
import com.agiletv.users.application.service.dto.UserGeneratorDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class RandomUserGenerateServiceTest {

    @Autowired
    private RandomUserGenerateService service;

    @Test
    void generateUsersTest() throws UserGeneratorServiceException {
        Integer quantityUsersToGenerate = 2;
        List<UserGeneratorDto> users = service.generateUsers(quantityUsersToGenerate);

        assertThat(users).hasSize(quantityUsersToGenerate);
        users.forEach(user -> {
            assertNotNull(user.getLogin());
            assertNotNull(user.getLogin().getUsername());
            assertNotNull(user.getEmail());
            assertNotNull(user.getName());
            assertNotNull(user.getGender());
            assertNotNull(user.getPicture());
            assertNotNull(user.getPicture().getLarge());
            assertNotNull(user.getPicture().getThumbnail());
            assertNotNull(user.getPicture().getMedium());
            assertNotNull(user.getLocation());
            assertNotNull(user.getLocation().getCity());
            assertNotNull(user.getLocation().getCountry());
            assertNotNull(user.getLocation().getState());
        });
    }
}