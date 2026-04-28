package com.oscarfndez.inventory.adapters.persistence.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameEntity {

    @Id
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "game_platform",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private List<PlatformEntity> platforms = new ArrayList<>();
}
