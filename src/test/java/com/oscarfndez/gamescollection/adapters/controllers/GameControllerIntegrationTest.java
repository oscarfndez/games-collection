package com.oscarfndez.gamescollection.adapters.controllers;

import com.oscarfndez.framework.core.model.auth.Role;
import com.oscarfndez.framework.core.model.auth.User;
import com.oscarfndez.gamescollection.adapters.persistence.entities.GameEntity;
import com.oscarfndez.gamescollection.adapters.rest.dtos.GameDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@Transactional
public class GameControllerIntegrationTest extends IntegrationTest {

    @Test
    public void when_retrievingAnExistingGameItGetsReturned() throws Exception {

        // Given an existing Game
        // When performing a GET http request by id
        User loggedUser = createUser("user@domain.com", Role.USER);
        this.mockMvc.perform(get("/api/game?id="+ RAYMAN_LEGENDS_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeaders(loggedUser)))
                .andDo(print())
                // then http status code of the response is Ok
                .andExpect(status().isOk())
                // and the game is returned
                .andExpect(assertJsonPath("$.id", RAYMAN_LEGENDS_ID))
                .andExpect(assertJsonPath("$.name", RAYMAN_LEGENDS))
                .andExpect(assertJsonPath("$.description", RAYMAN_LEGENDS_DESCRIPTION))
                .andExpect(assertJsonPath("$.platform_id", NINTENDO_WII_U_ID));

        // And the returned game is the same as the one stored in the database with the id in the request params
        GameEntity gameEntity = gameJpaRepository.findById(UUID.fromString(RAYMAN_LEGENDS_ID)).orElseThrow();
        assertEquals(RAYMAN_LEGENDS, gameEntity.getName());
        assertEquals(RAYMAN_LEGENDS_DESCRIPTION, gameEntity.getDescription());
    }

    @Test
    public void when_retrievingAllExistingGamesTheyGetReturned() throws Exception {

        // Given some existing Games
        // When performing a GET http request for all games
        User loggedUser = createUser("user@domain.com", Role.USER);
        this.mockMvc.perform(get("/api/game/all")
                        .contentType(MediaType.APPLICATION_JSON)
                .headers(buildHeaders(loggedUser)))
                .andDo(print())
                // then http status code of the response is Ok
                .andExpect(status().isOk())
                // and all games are returned
                .andExpect(assertJsonPath("$[0].id", WONDERFUL_101_ID))
                .andExpect(assertJsonPath("$[0].name", WONDERFUL_101))
                .andExpect(assertJsonPath("$[0].description", WONDERFUL_101_DESCRIPTION))
                .andExpect(assertJsonPath("$[1].id", RAYMAN_LEGENDS_ID))
                .andExpect(assertJsonPath("$[1].name", RAYMAN_LEGENDS))
                .andExpect(assertJsonPath("$[1].description", RAYMAN_LEGENDS_DESCRIPTION));

        // And the returned platforms are the same as the ones stored in the database
        List<GameEntity> gameEntities = gameJpaRepository.findAll();
        assertEquals(2, gameEntities.size());
        assertEquals(WONDERFUL_101_ID, gameEntities.get(0).getId().toString());
        assertEquals(WONDERFUL_101, gameEntities.get(0).getName());
        assertEquals(WONDERFUL_101_DESCRIPTION, gameEntities.get(0).getDescription());
        assertEquals(RAYMAN_LEGENDS_ID, gameEntities.get(1).getId().toString());
        assertEquals(RAYMAN_LEGENDS, gameEntities.get(1).getName());
        assertEquals(RAYMAN_LEGENDS_DESCRIPTION, gameEntities.get(1).getDescription());
    }

    @Test
    public void when_postingAGameItGetsStoredAndReturned() throws Exception {

        // Given a new Game
        GameDto dto = GameDto
                .builder()
                .id(UUID.randomUUID())
                .name(WINDWAKER)
                .description(WINDWAKER_DESCRIPTION)
                .platformId(UUID.fromString(NINTENDO_WII_U_ID))
                .build();

        // When performing a http REST POST request to create the new game
        User loggedUser = createUser("user@domain.com", Role.USER);
        ResultActions result = this.mockMvc.perform(post("/api/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeaders(loggedUser))
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                // then http status code of the response is created
                .andExpect(status().isCreated())
                // and the new game is returned
                .andExpect(assertJsonPath("$.name", WINDWAKER))
                .andExpect(assertJsonPath("$.description", WINDWAKER_DESCRIPTION))
                .andExpect(assertJsonPath("$.platform_id", NINTENDO_WII_U_ID));

        // And the stored game is the same as the one in the POST request payload
        GameEntity gameEntity = gameJpaRepository.findById(
                objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), GameDto.class).getId()).orElseThrow();
        assertEquals(WINDWAKER, gameEntity.getName());
        assertEquals(WINDWAKER_DESCRIPTION, gameEntity.getDescription());
        assertEquals(NINTENDO_WII_U_ID, gameEntity.getPlatform().getId().toString());
    }

    @Test
    public void when_updatingAGameItGetsStoredAndReturned() throws Exception {

        // Given an existing Game
        // When performing a http REST PUT request to the game
        User loggedUser = createUser("user@domain.com", Role.USER);
        GameDto gameDto = GameDto
                .builder()
                .id(UUID.fromString(RAYMAN_LEGENDS_ID))
                .name(RAYMAN_LEGENDS)
                .description(RAYMAN_LEGENDS_DESCRIPTION_MODIFIED)
                .platformId(UUID.fromString(NINTENDO_WII_U_ID))
                .build();

        this.mockMvc.perform(put("/api/game?id="+ RAYMAN_LEGENDS_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(buildHeaders(loggedUser))
                .content(objectMapper.writeValueAsString(gameDto)))
                .andDo(print())
                // then http status code of the response is created
                .andExpect(status().isCreated())
                // game is updated and returned
                .andExpect(assertJsonPath("$.name", RAYMAN_LEGENDS))
                .andExpect(assertJsonPath("$.description", RAYMAN_LEGENDS_DESCRIPTION_MODIFIED))
                .andExpect(assertJsonPath("$.platform_id", NINTENDO_WII_U_ID));

        // And the game is successfully updated and the same as the one in the PUT request payload
        GameEntity gameEntity = gameJpaRepository.findById(UUID.fromString(RAYMAN_LEGENDS_ID)).orElseThrow();
        assertEquals(RAYMAN_LEGENDS, gameEntity.getName());
        assertEquals(RAYMAN_LEGENDS_DESCRIPTION_MODIFIED, gameEntity.getDescription());
        assertEquals(NINTENDO_WII_U_ID, gameEntity.getPlatform().getId().toString());
    }

    @Test
    public void when_deletingAnExistingGameItGetDeletedAndNoContentStatusIsReturned() throws Exception {

        // Given an existing Game
        // When performing a DELETE http request by id
        User loggedUser = createUser("user@domain.com", Role.USER);
        this.mockMvc.perform(delete("/api/game?id="+ RAYMAN_LEGENDS_ID)
                .headers(buildHeaders(loggedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // then http status code of the response is no content
                .andExpect(status().isNoContent());
    }
}
