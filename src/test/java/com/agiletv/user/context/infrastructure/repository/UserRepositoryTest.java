package com.agiletv.user.context.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.agiletv.user.app.config.TestConfig;
import com.agiletv.user.context.infrastructure.domain.UserEntity;
import java.util.List;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
        repository.deleteById(this.username);
    }

    @Test
    void findByIdOKTest() {
        Optional<UserEntity> user = repository.findById(this.username);
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(this.username);
    }

    @Test
    void findByIdKOTest() {
        Optional<UserEntity> user = repository.findById("not_found");
        assertThat(user).isEmpty();
    }

    @Test
    void findAllTest() {
        List<UserEntity> users = repository.findAll();
        assertThat(users).hasSize(1);

        users.forEach(user -> assertThat(user.getUsername()).isEqualTo(this.username));
    }

    @Test
    void findAllPageableTest() {
        Sort sort = Sort.by("username").descending();
        Page<UserEntity> users = repository.findAll(PageRequest.of(0, 1, sort));
        assertThat(users.getTotalElements()).isEqualTo(1);
        assertThat(users.getNumber()).isZero();
        assertThat(users.getSize()).isEqualTo(1);
        assertThat(users.getContent()).hasSize(1);

        users.getContent().forEach(user -> assertThat(user.getUsername()).isEqualTo(this.username));
    }
}