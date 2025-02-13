package com.oscarfndez.gamescollection.adapters.controllers;

import com.oscarfndez.gamescollection.adapters.persistence.entities.PlatformEntity;
import com.oscarfndez.gamescollection.adapters.rest.dtos.PlatformDto;
import com.oscarfndez.gamescollection.testutils.IntegrationTest;
import jakarta.transaction.Transactional;
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

@Transactional
public class PlatformControllerIntegrationTest extends IntegrationTest {

    @Test
    public void when_retrievingAnExistingPlatformItGetsReturned() throws Exception {

        // Given an existing Platform
        // When performing a GET http request by id
        // then the platform is returned
        this.mockMvc.perform(get("/api/platform?id="+ NINTENDO_WII_U_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
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
        // then all platforms are returned
        this.mockMvc.perform(get("/api/platform/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
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
        // then the new platform is returned
        ResultActions result = this.mockMvc.perform(post("/api/platform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(assertJsonPath("$.name", SEGA_MEGADRIVE))
                .andExpect(assertJsonPath("$.description", SEGA_MEGADRIVE_DESCRIPTION))
                .andDo(print())
                .andExpect(status().isCreated());

        // And the stored platform is the same as the one in the POST request payload
        PlatformEntity platformEntity = platformJpaRepository.findById(
                objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), PlatformDto.class).getId()).orElseThrow();
        assertEquals(SEGA_MEGADRIVE, platformEntity.getName());
        assertEquals(SEGA_MEGADRIVE_DESCRIPTION, platformEntity.getDescription());
    }

    @Test
    public void when_updatingAPlatformItGetsStoredAndReturned() throws Exception {

        // Given an existing Platform
        // When performing a http REST PUT request to the platform
        // then platform is returned and updated
        PlatformDto dto = PlatformDto.builder().id(UUID.fromString(NINTENDO_WII_U_ID)).name(NINTENDO_WII_U).description(NINTENDO_WII_U_UPDATED_DESCRIPTION).build();

        this.mockMvc.perform(put("/api/platform?id="+ NINTENDO_WII_U_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(assertJsonPath("$.name", NINTENDO_WII_U))
                .andExpect(assertJsonPath("$.description", NINTENDO_WII_U_UPDATED_DESCRIPTION))
                .andExpect(status().isCreated());

        // And the stored platform is successfully updated and the same as the one in the PUT request payload
        PlatformEntity platformEntity = platformJpaRepository.findById(UUID.fromString(NINTENDO_WII_U_ID)).orElseThrow();
        assertEquals(NINTENDO_WII_U, platformEntity.getName());
        assertEquals(NINTENDO_WII_U_UPDATED_DESCRIPTION, platformEntity.getDescription());
    }

    @Test
    public void when_deletingAnExistingPlatformItGetDeletedAndNoContentStatusIsReturned() throws Exception {

        // Given an existing Platform
        // When performing a DELETE http request by id
        // then the platform is returned
        this.mockMvc.perform(delete("/api/platform?id="+ NINTENDO_WII_U_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        // And the returned platform is no longer in the database
        assertFalse(platformJpaRepository.findById(UUID.fromString(NINTENDO_WII_U_ID)).isPresent());
    }
}
