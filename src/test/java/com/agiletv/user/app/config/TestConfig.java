package com.agiletv.user.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.agiletv.user.context.infrastructure.repository")
@ComponentScan(basePackages = "com.agiletv.user")
@EntityScan(basePackages = "com.agiletv.user.context.infrastructure.domain")
public class TestConfig { }
