package com.oscarfndez.inventory.integration;

import com.oscarfndez.framework.core.model.auth.Role;
import com.oscarfndez.framework.core.model.auth.User;
import com.oscarfndez.framework.core.services.auth.JwtService;
import com.oscarfndez.inventory.adapters.rest.dtos.GameDto;
import com.oscarfndez.inventory.adapters.rest.dtos.PlatformDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryFlowIntegrationTest extends AbstractPostgresIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    @Test
    void platformCanBeCreatedAndLoadedWithAuthenticatedUser() {
        HttpHeaders headers = authHeadersFor("platform-it@example.com");
        HttpEntity<PlatformDto> request = new HttpEntity<>(
                PlatformDto.builder()
                        .name("Nintendo Switch")
                        .description("Console")
                        .imageUrl("switch.png")
                        .build(),
                headers);

        var createResponse = restTemplate.postForEntity("/api/platform", request, PlatformDto.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getId()).isNotNull();

        var loadResponse = restTemplate.exchange(
                "/api/platform?id=" + createResponse.getBody().getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                PlatformDto.class);

        assertThat(loadResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loadResponse.getBody()).isNotNull();
        assertThat(loadResponse.getBody().getName()).isEqualTo("Nintendo Switch");
    }

    @Test
    void gameCanBeCreatedAndListedWithAuthenticatedUser() {
        HttpHeaders headers = authHeadersFor("game-it@example.com");
        UUID platformId = createPlatform(headers);
        GameDto gameRequest = GameDto.builder()
                .name("Zelda")
                .description("Adventure")
                .platformIds(java.util.List.of(platformId))
                .imageUrl("zelda.png")
                .build();

        var createResponse = restTemplate.postForEntity(
                "/api/game",
                new HttpEntity<>(gameRequest, headers),
                GameDto.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getName()).isEqualTo("Zelda");

        var listResponse = restTemplate.exchange(
                "/api/game/all?search=zelda&page=0&size=10&sortField=name&sortDir=asc",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).contains("Zelda");
    }

    @Test
    void protectedEndpointReturnsUnauthorizedWithoutJwt() {
        var response = restTemplate.getForEntity("/api/platform/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private UUID createPlatform(HttpHeaders headers) {
        PlatformDto platformRequest = PlatformDto.builder()
                .name("Nintendo Switch")
                .description("Console")
                .imageUrl("switch.png")
                .build();
        var response = restTemplate.postForEntity(
                "/api/platform",
                new HttpEntity<>(platformRequest, headers),
                PlatformDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        return response.getBody().getId();
    }

    private HttpHeaders authHeadersFor(String email) {
        User user = User.builder()
                .firstName("Integration")
                .lastName("Test")
                .email(email)
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
