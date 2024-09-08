package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.repository.AuthorRepository;
import com.u238.recipeApi.util.AuthorMapper;
import com.u238.recipeApi.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

//todo add logging
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements CrudService<AuthorDto>{

    private final AuthorRepository repository;

    @Override
    public AuthorDto read(Long id) {
        if(id<=0) throw new IllegalArgumentException();
        Optional<Author> tagOptional = repository.findById(id);
        if(tagOptional.isPresent()){
            return AuthorMapper.toDto(tagOptional.get());
        }else throw new NullPointerException();
    }

    @Override
    public Collection<AuthorDto> readAll() {
        return AuthorMapper.toDtoCollection(repository.findAll());
    }

    @Override
    public AuthorDto create(AuthorDto dto) {
        if(repository.existsByAuthorName(dto.getAuthorName())) throw new IllegalStateException();
        if(StringUtils.hasDisallowedCharacters(dto.getAuthorName())) throw new IllegalArgumentException();
        return AuthorMapper.toDto(repository.save(AuthorMapper.toEntity(dto)));
    }

    @Override
    public AuthorDto update(Long id, AuthorDto dto) {
        if(repository.existsByAuthorName(dto.getAuthorName())) throw new IllegalStateException();
        if(StringUtils.hasDisallowedCharacters(dto.getAuthorName())) throw new IllegalArgumentException();
        Optional<Author>authorOptional = repository.findById(id);
        if(authorOptional.isEmpty()) throw new NullPointerException();

        Author found = authorOptional.get();

        found.setAuthorName(dto.getAuthorName());

        return AuthorMapper.toDto(repository.save(found));
    }

    @Override
    public void delete(Long id) {
        Optional<Author>authorOptional = repository.findById(id);
        if(authorOptional.isEmpty()) throw new NullPointerException();
        repository.delete(authorOptional.get());
    }
}
