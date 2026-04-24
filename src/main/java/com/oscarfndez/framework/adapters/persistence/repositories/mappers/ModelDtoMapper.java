package com.oscarfndez.framework.adapters.persistence.repositories.mappers;

public interface ModelDtoMapper <M, D> {

    D mapToDTO(M model);
}
