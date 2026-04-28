package com.oscarfndez.inventory.adapters.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameItemDto {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("game_id")
    private UUID gameId;
    @JsonProperty("game_name")
    private String gameName;
    @JsonProperty("platform_id")
    private UUID platformId;
    @JsonProperty("platform_name")
    private String platformName;
}

