package com.oscarfndez.inventory.adapters.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "studio")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudioEntity {

    @Id
    private UUID id;
    private String name;
    private String description;
    private String location;
    private Boolean firstParty;

    @OneToMany(mappedBy = "studio")
    private List<GameEntity> games = new ArrayList<>();
}
