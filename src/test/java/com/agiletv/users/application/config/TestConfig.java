package com.agiletv.users.application.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.agiletv.users.infrastructure.persistence.repository.jpa")
@ComponentScan(basePackages = "com.agiletv.users")
@EntityScan(basePackages = "com.agiletv.users.infrastructure.persistence.entity")
public class TestConfig { }
