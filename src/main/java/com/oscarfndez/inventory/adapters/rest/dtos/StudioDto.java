package com.oscarfndez.inventory.adapters.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudioDto {

    @JsonProperty("id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("location")
    private String location;
    @JsonProperty("first_party")
    private Boolean firstParty;
    @JsonProperty("game_ids")
    private List<UUID> gameIds;
    @JsonProperty("game_names")
    private List<String> gameNames;
    @JsonProperty("games_count")
    private int gamesCount;
}
