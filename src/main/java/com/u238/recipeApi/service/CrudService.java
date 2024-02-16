package com.u238.recipeApi.service;

import com.u238.recipeApi.Dto.Dto;

import java.util.Collection;

public interface CrudService <T extends Dto > {

    T create(T dto);

    T read(Long id);

    Collection<T> readAll();

    T update(Long id, T dto);

    void delete(Long id);

}
