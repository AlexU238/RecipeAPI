package com.u238.recipeApi.service;

import com.u238.recipeApi.Dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Tag;
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
        }
        return null;
    }

    @Override
    public AuthorDto read(Long id) {
        Optional<Author> tagOptional = repository.findById(id);
        return tagOptional.map(mapper::toDto).orElse(null);
    }

    @Override
    public Collection<AuthorDto> readAll() {
        return mapper.toDtoCollection(repository.findAll());
    }

    @Override
    public AuthorDto update(Long id, AuthorDto dto) {
        Optional<Author> tagOptional = repository.findById(id);
        if(tagOptional.isPresent()){
            Author author = mapper.toEntity(dto);
            author.setAuthorId(tagOptional.get().getAuthorId());
            return mapper.toDto(repository.save(author));
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        Optional<Author>tagOptional=repository.findById(id);
        if(tagOptional.isPresent()) repository.deleteById(id);
    }
}
