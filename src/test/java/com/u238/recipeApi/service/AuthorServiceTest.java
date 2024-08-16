package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Recipe;
import com.u238.recipeApi.entity.Tag;
import com.u238.recipeApi.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    Collection<TagDto> testTagsDto;
    Collection<Tag> testTags;
    AuthorDto testAuthorDto;
    Author testAuthor;
    RecipeDto testRecipeDto1;
    TagDto tagDto1;
    Recipe testRecipe1;
    Tag tag1;
    Collection<Recipe> recipes;
    ArrayList<RecipeDto> recipesDto;

    @BeforeEach
    void setUp() {
        recipes = new ArrayList<>();
        recipesDto = new ArrayList<>();
        testTags = new ArrayList<Tag>();
        tag1 = Tag.builder().tagId(1L).tagName("TEST TAG 1").build();

        testRecipe1 = Recipe.builder()
                .recipeId(1L)
                .recipeName("OMLETTE")
                .isValid(false)
                .tags(testTags)
                .ingredients("eggs, milk")
                .recipe("mix together and fry")
                .author(testAuthor)
                .build();
        recipes.add(testRecipe1);

        testAuthor = Author.builder().authorId(1L).authorName("Test").recipes(recipes).build();

        testTagsDto = new ArrayList<TagDto>();
        tagDto1 = TagDto.builder().tagId(1L).tagName("TEST TAG 1").build();
        testTagsDto.add(tagDto1);
        testRecipeDto1 = RecipeDto.builder()
                .recipeId(1L)
                .recipeName("Omlette")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("Eggs, milk")
                .recipe("Mix together and fry")
                .authorId(testAuthor.getAuthorId())
                .authorName(testAuthor.getAuthorName())
                .build();
        recipesDto.add(testRecipeDto1);

        testAuthorDto = AuthorDto.builder().authorId(1L).authorName("Test").recipes(recipesDto).build();

    }

    @Test
    void testRead() {
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(testAuthor));
        AuthorDto result = authorService.read(authorId);
        verify(authorRepository, atLeastOnce()).findById(1L);
    }

    @Test
    void testReadNotFound() {
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            authorService.read(1L);
        });
        assertNotNull(exception);
    }

}
