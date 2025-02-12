package com.framework.architecture.hexagonal.utils;

public interface ModelDtoMapper <M, D> {

    D mapToDTO(M model);
}
