package com.gamescollection.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record GameDTO(@JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY) UUID id
        , @JsonProperty("name") String name
        , @JsonProperty("description") String description) {
}
