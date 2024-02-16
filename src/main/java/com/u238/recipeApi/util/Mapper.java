package com.u238.recipeApi.util;

import java.util.Collection;

public interface Mapper<T, D>{

    T toDto(D entity);

    D toEntity(T dto);

    Collection<T> toDtoCollection(Collection<D>entityCollection);

    Collection<D> toEntityCollection(Collection<T>dtoCollection);

}
