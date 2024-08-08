package com.u238.recipeApi.util;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorMapperTest {

    Author author1 = Author.builder().authorId(1L).authorName("Test1").build();
    Author author2 = Author.builder().authorId(2L).authorName("Test2").build();
    AuthorDto authorDto1 = AuthorDto.builder().authorId(1L).authorName("Test1").build();
    AuthorDto authorDto2 = AuthorDto.builder().authorId(2L).authorName("Test2").build();


    @Test
    void testToDto(){
        AuthorDto result = AuthorMapper.toDto(author1);

        assertNotNull(result);
        assertEquals(result.getAuthorId(),author1.getAuthorId());
        assertEquals(result.getAuthorName(),author1.getAuthorName());
    }

    @Test
    void testToEntity(){
        Author result = AuthorMapper.toEntity(authorDto1);

        assertNotNull(result);
        assertEquals(result.getAuthorId(),authorDto1.getAuthorId());
        assertEquals(result.getAuthorName(),authorDto1.getAuthorName());
    }

    @Test
    void testToDtoCollection(){
        ArrayList<Author> authors = new ArrayList<>();
        authors.add(author1);
        authors.add(author2);

        Collection<AuthorDto>result = AuthorMapper.toDtoCollection(authors);

        assertNotNull(result);
        assertTrue(result.contains(authorDto1));
        assertTrue(result.contains(authorDto2));
    }

    @Test
    void testToEntityCollection(){
        ArrayList<AuthorDto>authorDtos = new ArrayList<>();
        authorDtos.add(authorDto1);
        authorDtos.add(authorDto2);

        Collection<Author>result = AuthorMapper.toEntityCollection(authorDtos);

        assertNotNull(result);
        assertTrue(result.contains(author1));
        assertTrue(result.contains(author2));
    }

}
