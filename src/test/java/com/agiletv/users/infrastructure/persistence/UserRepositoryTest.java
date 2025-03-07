package com.agiletv.users.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.agiletv.users.application.config.TestConfig;
import com.agiletv.users.domain.model.IUser;
import com.agiletv.users.domain.model.PagedResult;
import com.agiletv.users.infrastructure.persistence.entity.UserEntity;
import com.agiletv.users.infrastructure.persistence.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private String username;

    @BeforeEach
    public void setUp() {
        UserEntity user = new EasyRandom().nextObject(UserEntity.class);
        this.username = user.getUsername();

        repository.save(user);
    }

    @AfterEach
    public void tearDown() {
        repository.deleteByUsername(this.username);
    }

    @Test
    void findByIdOKTest() {
        Optional<IUser> user = repository.findByUsername(this.username);
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(this.username);
    }

    @Test
    void findByIdKOTest() {
        Optional<IUser> user = repository.findByUsername("not_found");
        assertThat(user).isEmpty();
    }

    @Test
    void findAllTest() {
        List<IUser> users = repository.findAll();
        assertThat(users).hasSize(1);

        users.forEach(user -> assertThat(user.getUsername()).isEqualTo(this.username));
    }

    @Test
    void findAllPageableTest() {
        PagedResult<IUser> users = repository.findAll(1, 1);
        assertThat(users.getTotalElements()).isEqualTo(1);
        assertThat(users.getPage()).isEqualTo(1);
        assertThat(users.getSize()).isEqualTo(1);
        assertThat(users.getContent()).hasSize(1);

        users.getContent().forEach(user -> assertThat(user.getUsername()).isEqualTo(this.username));
    }
}