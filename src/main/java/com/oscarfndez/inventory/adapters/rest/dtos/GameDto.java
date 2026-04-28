package com.oscarfndez.inventory.adapters.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    @JsonProperty(value = "id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("platform_id")
    private UUID platformId;
    @JsonProperty("platform_name")
    private String platformName;
    @JsonProperty("platform_ids")
    private List<UUID> platformIds;
    @JsonProperty("platform_names")
    private List<String> platformNames;
    @JsonProperty("image_url")
    private String imageUrl;
}
