package com.u238.recipeApi.service;

import com.u238.recipeApi.Dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.repository.AuthorRepository;
import com.u238.recipeApi.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AuthorService implements CrudService<AuthorDto> {

    private final AuthorRepository repository;
    private final Mapper<AuthorDto,Author> mapper;

    @Autowired
    public AuthorService(@Qualifier("authorRepository") AuthorRepository repository,
                         @Qualifier("authorMapper") Mapper<AuthorDto, Author> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AuthorDto create(AuthorDto dto) {
        Optional<Author>authorOptional = repository.getByAuthorName(dto.getAuthorName());
        if(authorOptional.isEmpty()){
            return mapper.toDto(repository.save(mapper.toEntity(dto)));
        }else throw new IllegalStateException();
    }

    @Override
    public AuthorDto read(Long id) {
        Optional<Author> tagOptional = repository.findById(id);
        if(tagOptional.isPresent()){
            return mapper.toDto(tagOptional.get());
        }else throw new NullPointerException();
    }

    @Override
    public Collection<AuthorDto> readAll() {
        return mapper.toDtoCollection(repository.findAll());
    }

    @Override
    public AuthorDto update(Long id, AuthorDto dto) {
        Optional<Author> authorOptional = repository.findById(id);
        if(authorOptional.isPresent()){
            Author author = mapper.toEntity(dto);
            author.setAuthorId(dto.getAuthorId());
            return mapper.toDto(repository.save(author));
        }else throw new NullPointerException();
    }

    @Override
    public void delete(Long id) {
        Optional<Author>tagOptional=repository.findById(id);
        if(tagOptional.isPresent()){
            repository.delete(tagOptional.get());
        }else throw new NullPointerException();
    }
}
