package com.oscarfndez.gamescollection.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class Platform {

    private UUID id;
    private String name;
    private String description;

}
