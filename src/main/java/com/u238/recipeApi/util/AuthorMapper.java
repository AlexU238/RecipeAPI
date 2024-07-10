package com.u238.recipeApi.util;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AuthorMapper implements Mapper<AuthorDto, Author> {
    @Override
    public AuthorDto toDto(Author entity) {
        return AuthorDto.builder().authorId(entity.getAuthorId()).authorName(entity.getAuthorName()).build();
    }

    @Override
    public Author toEntity(AuthorDto dto) {
        return Author.builder().authorId(dto.getAuthorId()).authorName(dto.getAuthorName()).build();
    }

    @Override
    public Collection<AuthorDto> toDtoCollection(Collection<Author> entityCollection) {
        return entityCollection.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Author> toEntityCollection(Collection<AuthorDto> dtoCollection) {
        return dtoCollection.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
