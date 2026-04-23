package com.oscarfndez.framework.core.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WhoAmIDto {

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;
}