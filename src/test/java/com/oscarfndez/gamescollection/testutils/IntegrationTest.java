package com.oscarfndez.gamescollection.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarfndez.framework.core.model.auth.Role;
import com.oscarfndez.framework.core.model.auth.User;
import com.oscarfndez.framework.adapters.persistence.auth.UserRepository;
import com.oscarfndez.framework.core.services.auth.impl.JwtServiceImpl;
import com.oscarfndez.gamescollection.adapters.persistence.GameJpaRepository;
import com.oscarfndez.gamescollection.adapters.persistence.PlatformJpaRepository;
import com.oscarfndez.gamescollection.config.JwtAuthenticationFilter;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class IntegrationTest {

    public static final String FIRST_NAME = "Alan";
    public static final String LAST_NAME = "Turing";
    public static final String PASSWORD = "enigma";
    public static final String TOKEN_HEADER_NAME = "Authorization";

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected PlatformJpaRepository platformJpaRepository;

    @Autowired
    protected GameJpaRepository gameJpaRepository;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    protected String basePath;

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("test")
            .withUsername("username")
            .withPassword("yo");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilter(jwtAuthenticationFilter).build();

        Flyway flyway = Flyway.configure()
                .dataSource(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())
                .load();
        flyway.migrate();
    }

    @NotNull
    protected static ResultMatcher assertJsonPath(String jsonPathExpression, String expectedValue) {
        return MockMvcResultMatchers.jsonPath(jsonPathExpression).value(expectedValue);
    }


    protected User createUser(final String email, Role role) {
        var user = User.builder().firstName(FIRST_NAME).lastName(LAST_NAME)
                .email(email).password(passwordEncoder.encode(PASSWORD))
                .role(role).build();
        return userRepository.saveAndFlush(user);
    }

    protected HttpHeaders buildHeaders(User user) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(TOKEN_HEADER_NAME, jwtService.generateToken(user));
        return httpHeaders;
    }
}
