package com.u238.recipeApi.util;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AuthorMapper{

    public static AuthorDto toDto(Author entity) {
        return AuthorDto.builder().authorId(entity.getAuthorId()).authorName(entity.getAuthorName()).build();
    }

    public static Author toEntity(AuthorDto dto) {
        return Author.builder().authorId(dto.getAuthorId()).authorName(dto.getAuthorName()).build();
    }

    public static Collection<AuthorDto> toDtoCollection(Collection<Author> entityCollection) {
        return entityCollection.stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Collection<Author> toEntityCollection(Collection<AuthorDto> dtoCollection) {
        return dtoCollection.stream()
                .map(AuthorMapper::toEntity)
                .collect(Collectors.toList());
    }
}
