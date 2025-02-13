package com.oscarfndez.gamescollection.adapters.persistence.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    private PlatformEntity platform;
}
