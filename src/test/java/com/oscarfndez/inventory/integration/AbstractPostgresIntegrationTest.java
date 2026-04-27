package com.oscarfndez.inventory.integration;

import com.oscarfndez.framework.core.ports.repositories.UserRepository;
import com.oscarfndez.inventory.ports.repositories.GameJpaRepository;
import com.oscarfndez.inventory.ports.repositories.PlatformJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractPostgresIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("inventory")
            .withUsername("inventory")
            .withPassword("inventory");

    @Autowired
    protected GameJpaRepository gameJpaRepository;

    @Autowired
    protected PlatformJpaRepository platformJpaRepository;

    @Autowired
    protected UserRepository userRepository;

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
    }

    @AfterEach
    void cleanUpCreatedData() {
        gameJpaRepository.deleteAll();
        platformJpaRepository.deleteAll();
        userRepository.deleteAll();
    }
}
