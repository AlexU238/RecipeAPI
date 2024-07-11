package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.repository.AuthorRepository;
import com.u238.recipeApi.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

//todo add logging

@Service
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository repository;
    private final Mapper<AuthorDto,Author> mapper;

    @Autowired
    public AuthorServiceImpl(@Qualifier("authorRepository") AuthorRepository repository,
                             @Qualifier("authorMapper") Mapper<AuthorDto, Author> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public AuthorDto read(Long id) {
        Optional<Author> tagOptional = repository.findById(id);
        if(tagOptional.isPresent()){
            return mapper.toDto(tagOptional.get());
        }else throw new NullPointerException();
    }

    public Collection<AuthorDto> readAll() {
        return mapper.toDtoCollection(repository.findAll());
    }

}
