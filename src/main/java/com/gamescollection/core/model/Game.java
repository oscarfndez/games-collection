package com.gamescollection.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class Game {
    private UUID id;
    private String name;
    private String description;
    private Platform platform;
}
