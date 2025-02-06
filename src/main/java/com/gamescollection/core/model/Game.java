package com.gamescollection.core.model;

import java.util.UUID;

public record Game(
    UUID id,
    String name,
    String description
) {
}
