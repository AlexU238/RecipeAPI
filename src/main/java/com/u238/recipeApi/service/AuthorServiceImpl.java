package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.repository.AuthorRepository;
import com.u238.recipeApi.util.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

//todo add logging
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository repository;

    public AuthorDto read(Long id) {
        Optional<Author> tagOptional = repository.findById(id);
        if(tagOptional.isPresent()){
            return AuthorMapper.toDto(tagOptional.get());
        }else throw new NullPointerException();
    }

    public Collection<AuthorDto> readAll() {
        return AuthorMapper.toDtoCollection(repository.findAll());
    }

}
