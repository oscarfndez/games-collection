package com.oscarfndez.inventory.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class Studio {

    private UUID id;
    private String name;
    private String description;
    private String location;
    private Boolean firstParty;
    private List<Game> games;
}
