package com.gamescollection.adapters.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "platform")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlatformEntity {
    @Id
    private UUID id;
    private String name;
    private String description;
}
