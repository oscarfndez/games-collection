package com.oscarfndez.framework.adapters.persistence;

public interface ModelEntityMapper <M, E> {

    E modelToEntity(M model);
    M entityToModel(E entity);
}
