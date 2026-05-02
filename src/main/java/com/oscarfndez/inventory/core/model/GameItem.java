package com.oscarfndez.inventory.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class GameItem {
    private UUID id;
    private UUID userId;
    private Game game;
    private Platform platform;
    private boolean active;
}
