package com.oscarfndez.gamescollection.adapters.controllers;

import com.oscarfndez.framework.core.model.auth.Role;
import com.oscarfndez.framework.core.model.auth.User;
import com.oscarfndez.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.gamescollection.adapters.rest.dtos.PlatformDto;
import com.oscarfndez.gamescollection.testutils.IntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static com.oscarfndez.gamescollection.testutils.TestDataConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@Transactional
public class PlatformControllerIntegrationTest extends IntegrationTest {

    @Test
    public void when_retrievingAnExistingPlatformItGetsReturned() throws Exception {

        // Given an existing Platform
        // When performing a GET http request by id
        User loggedUser = createUser("user@domain.com", Role.ADMIN);
        this.mockMvc.perform(get("/api/platform?id="+ NINTENDO_WII_U_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeaders(loggedUser)))
                .andDo(print())
                // then http status of the response is ok
                .andExpect(status().isOk())
                // and the platform is returned
                .andExpect(assertJsonPath("$.id", NINTENDO_WII_U_ID))
                .andExpect(assertJsonPath("$.name", NINTENDO_WII_U))
                .andExpect(assertJsonPath("$.description", NINTENDO_WII_U_DESCRIPTION));

        // And the returned platform is the same as the ones stored in the database
        PlatformEntity platformEntity = platformJpaRepository.findById(UUID.fromString(NINTENDO_WII_U_ID)).orElseThrow();
        assertEquals(NINTENDO_WII_U, platformEntity.getName());
        assertEquals(NINTENDO_WII_U_DESCRIPTION, platformEntity.getDescription());
    }

    @Test
    public void when_retrievingAllExistingPlatformsTheyGetReturned() throws Exception {

        // Given some existing Platforms
        // When performing a GET http request for all platforms
        User loggedUser = createUser("user@domain.com", Role.ADMIN);
        this.mockMvc.perform(get("/api/platform/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeaders(loggedUser)))
                .andDo(print())
                .andExpect(status().isOk())
                // then all platforms are returned
                .andExpect(assertJsonPath("$[0].id", NINTENDO_WII_U_ID))
                .andExpect(assertJsonPath("$[0].name", NINTENDO_WII_U))
                .andExpect(assertJsonPath("$[0].description", NINTENDO_WII_U_DESCRIPTION))
                .andExpect(assertJsonPath("$[1].id", STEAM_ID))
                .andExpect(assertJsonPath("$[1].name", STEAM))
                .andExpect(assertJsonPath("$[1].description", STEAM_DESCRIPTION));

        // And the returned platforms are the same as the ones stored in the database
        List<PlatformEntity> platformEntites = platformJpaRepository.findAll();
        assertEquals(2, platformEntites.size());
        assertEquals(NINTENDO_WII_U_ID, platformEntites.get(0).getId().toString());
        assertEquals(NINTENDO_WII_U, platformEntites.get(0).getName());
        assertEquals(NINTENDO_WII_U_DESCRIPTION, platformEntites.get(0).getDescription());
        assertEquals(STEAM_ID, platformEntites.get(1).getId().toString());
        assertEquals(STEAM, platformEntites.get(1).getName());
        assertEquals(STEAM_DESCRIPTION, platformEntites.get(1).getDescription());
    }

    @Test
    public void when_postingAPlatformItGetsStoredAndReturned() throws Exception {

        // Given a new Platform
        PlatformDto dto = PlatformDto.builder().id(UUID.randomUUID()).name(SEGA_MEGADRIVE).description(SEGA_MEGADRIVE_DESCRIPTION).build();
        // When performing a http REST POST request to create the new platform
        User loggedUser = createUser("user@domain.com", Role.ADMIN);
        ResultActions result = this.mockMvc.perform(post("/api/platform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeaders(loggedUser))
                .content(objectMapper.writeValueAsString(dto)))
                // then http status is created
                .andExpect(status().isCreated())
                // then the new platform is returned
                .andExpect(assertJsonPath("$.name", SEGA_MEGADRIVE))
                .andExpect(assertJsonPath("$.description", SEGA_MEGADRIVE_DESCRIPTION))
                .andDo(print());

        // And the stored platform is the same as the one in the POST request payload
        PlatformEntity platformEntity = platformJpaRepository.findById(
                objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), PlatformDto.class).getId()).orElseThrow();
        assertEquals(SEGA_MEGADRIVE, platformEntity.getName());
        assertEquals(SEGA_MEGADRIVE_DESCRIPTION, platformEntity.getDescription());
    }

    @Test
    public void when_updatingAPlatformItGetsStoredAndReturned() throws Exception {

        // Given an existing Platform
        PlatformDto dto = PlatformDto.builder().id(UUID.fromString(NINTENDO_WII_U_ID)).name(NINTENDO_WII_U).description(NINTENDO_WII_U_UPDATED_DESCRIPTION).build();
        // When performing a http REST PUT request to the platform
        User loggedUser = createUser("user@domain.com", Role.ADMIN);
        this.mockMvc.perform(put("/api/platform?id="+ NINTENDO_WII_U_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeaders(loggedUser))
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                // then http status is created
                .andExpect(status().isCreated())
                // then the new platform is returned
                .andExpect(assertJsonPath("$.name", NINTENDO_WII_U))
                .andExpect(assertJsonPath("$.description", NINTENDO_WII_U_UPDATED_DESCRIPTION));

        // And the stored platform is successfully updated and the same as the one in the PUT request payload
        PlatformEntity platformEntity = platformJpaRepository.findById(UUID.fromString(NINTENDO_WII_U_ID)).orElseThrow();
        assertEquals(NINTENDO_WII_U, platformEntity.getName());
        assertEquals(NINTENDO_WII_U_UPDATED_DESCRIPTION, platformEntity.getDescription());
    }

    @Test
    public void when_deletingAnExistingPlatformItGetDeletedAndNoContentStatusIsReturned() throws Exception {

        // Given an existing Platform
        // When performing a DELETE http request by id
        User loggedUser = createUser("user@domain.com", Role.ADMIN);
        this.mockMvc.perform(delete("/api/platform?id="+ NINTENDO_WII_U_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeaders(loggedUser)))
                .andDo(print())
                // then http code no content is returned
                .andExpect(status().isNoContent());

        // And the returned platform is no longer in the database
        assertFalse(platformJpaRepository.findById(UUID.fromString(NINTENDO_WII_U_ID)).isPresent());
    }
}
