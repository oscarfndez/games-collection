package com.oscarfndez.framework.adapters.persistence.mappers;

public interface ModelEntityMapper <M, E> {

    E modelToEntity(M model);
    M entityToModel(E entity);
}
