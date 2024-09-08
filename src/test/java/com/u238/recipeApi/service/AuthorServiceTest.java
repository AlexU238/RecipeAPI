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

import static org.junit.jupiter.api.Assertions.*;
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
    AuthorDto testAuthorDto2;
    AuthorDto testAuthorDtoUpd;
    Author testAuthor;
    Author testAuthor2;
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

        testAuthorDto2 = AuthorDto.builder().authorId(2L).authorName("Test2").build();
        testAuthor2 = Author.builder().authorId(2L).authorName("Test2").build();

        testAuthorDtoUpd = AuthorDto.builder().authorId(1L).authorName("Update").build();
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

    @Test
    void testCreateSuccess() {
        when(authorRepository.existsByAuthorName(testAuthor2.getAuthorName())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthorDto result = authorService.create(testAuthorDto2);

        assertNotNull(result);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testCreateNameExists() {
        when(authorRepository.existsByAuthorName(testAuthorDto2.getAuthorName())).thenReturn(true);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authorService.create(testAuthorDto2);
        });

        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testCreateWrongName1() {
        testAuthorDto2.setAuthorName("Invalid@Name");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.create(testAuthorDto2);
        });
        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testCreateWrongName2() {
        testAuthorDto2.setAuthorName("Another-User");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.create(testAuthorDto2);
        });
        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testCreateWrongName3(){
        testAuthorDto2.setAuthorName("user.name");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.create(testAuthorDto2);
        });
        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testUpdateSuccess(){
        when(authorRepository.existsByAuthorName(testAuthorDto2.getAuthorName())).thenReturn(false);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthorDto updated = authorService.update(1L,testAuthorDtoUpd);

        assertNotNull(updated);
        //todo test if recipes are still with author after update
        assertNotEquals(updated.getAuthorName(),testAuthorDto.getAuthorName());
    }

    @Test
    void testUpdateNameExists(){
        when(authorRepository.existsByAuthorName(testAuthorDtoUpd.getAuthorName())).thenReturn(true);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authorService.update(1L, testAuthorDtoUpd);
        });

        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testUpdateWrongName1(){
        testAuthorDtoUpd.setAuthorName("Invalid@Name");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.update(1L,testAuthorDtoUpd);
        });
        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testUpdateWrongName2(){
        testAuthorDtoUpd.setAuthorName("Another-User");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.update(1L,testAuthorDtoUpd);
        });
        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testUpdateWrongName3(){
        testAuthorDtoUpd.setAuthorName("user.name");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.update(1L,testAuthorDtoUpd);
        });
        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testUpdateNotFound(){
        when(authorRepository.existsByAuthorName(testAuthorDtoUpd.getAuthorName())).thenReturn(false);
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            authorService.update(1L,testAuthorDtoUpd);
        });
        assertNotNull(exception);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testDeleteSuccess(){
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(testAuthor));
        authorService.delete(authorId);

        verify(authorRepository, atMostOnce()).delete(any(Author.class));
    }

    @Test
    void testDeleteNotFound(){
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            authorService.delete(authorId);
        });
    }

}
