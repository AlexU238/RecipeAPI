package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.TagDto;

import java.util.Collection;

public interface TagService {

    TagDto create(TagDto dto);

    TagDto read(Long id);

    Collection<TagDto> readAll();

    void delete(Long id);
}
