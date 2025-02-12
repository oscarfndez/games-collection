package com.framework.architecture.hexagonal.persistence;

import com.gamescollection.core.model.Game;

public interface ModelEntityMapper <M, E> {

    E modelToEntity(M model);
    M entityToModel(E entity);
}
