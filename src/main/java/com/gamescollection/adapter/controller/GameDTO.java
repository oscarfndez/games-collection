package com.gamescollection.adapter.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Builder
@Data
public class GameDTO {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;

}
