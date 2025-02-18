package com.oscarfndez.gamescollection.adapters.controllers;

import com.oscarfndez.gamescollection.testutils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static com.oscarfndez.gamescollection.testutils.TestDataConstants.RAYMAN_LEGENDS_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestIntegrationTest extends IntegrationTest {

    @Test
    public void when_retrievingAnExistingGameButNoUserIsLoggedThenForbiddenIsReturned() throws Exception {

        // Given an existing Game
        // When performing a GET http request by id
        // And the request is performed with no logged in user
        // then the Forbidded is returned in the response status
        this.mockMvc.perform(get("/api/game?id="+ RAYMAN_LEGENDS_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    public void when_retrievingAnUnexistingResourceNotFoundIsReturned() throws Exception {

        basePath = "/api/platform";

        // Given an unexisting Resource Id
        // When performing a GET http request by id
        // then the game is not returned
        // and 404 status is returned
        this.mockMvc.perform(get(basePath+basePath+ "?id=" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
