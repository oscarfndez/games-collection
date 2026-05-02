package com.oscarfndez.inventory.adapters.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "game_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameItemEntity {

    @Id
    private UUID id;
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    private PlatformEntity platform;

    private boolean active;
}
