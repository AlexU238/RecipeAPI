package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.AuthorDto;

import java.util.Collection;

public interface AuthorService {

    AuthorDto read(Long id);

    Collection<AuthorDto> readAll();
}
