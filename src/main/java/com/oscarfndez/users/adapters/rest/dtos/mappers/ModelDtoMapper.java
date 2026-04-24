package com.oscarfndez.users.adapters.rest.dtos.mappers;

public interface ModelDtoMapper <M, D> {

    D mapToDTO(M model);
}
