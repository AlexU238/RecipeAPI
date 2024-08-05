package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.CollectionDto;
import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Recipe;
import com.u238.recipeApi.entity.Tag;
import com.u238.recipeApi.repository.AuthorRepository;
import com.u238.recipeApi.repository.RecipeRepository;
import com.u238.recipeApi.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private RecipeServiceImpl recipeService;

    Collection<TagDto>testTagsDto;
    Collection<Tag>testTags;
    RecipeDto testRecipeDto1;
    Recipe testRecipe1;
    Recipe testRecipe2;
    RecipeDto returnTestRecipeDto1;
    RecipeDto updatedRecipeDto;
    RecipeDto updatedRecipeDtoResult;
    Author testAuthor;
    TagDto tagDto1;
    TagDto tagDto2;
    Tag tag1;
    Tag tag2;

    @BeforeEach
    void setUp(){
        testTagsDto =new ArrayList<TagDto>();
        tagDto1=TagDto.builder().tagId(1L).tagName("TEST TAG 1").build();
        testTagsDto.add(tagDto1);
        tagDto2=TagDto.builder().tagId(2L).tagName("TEST TAG 2").build();
        testTagsDto.add(tagDto2);

        testTags = new ArrayList<Tag>();
        tag1=Tag.builder().tagId(1L).tagName("TEST TAG 1").build();
        testTags.add(tag1);
        tag2=Tag.builder().tagId(2L).tagName("TEST TAG 2").build();
        testTags.add(tag2);

        testAuthor=Author.builder().authorId(1L).authorName("Test").build();

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

        testRecipe1 = Recipe.builder()
                .recipeId(1L)
                .recipeName("OMLETTE")
                .isValid(false)
                .tags(testTags)
                .ingredients("eggs, milk")
                .recipe("mix together and fry")
                .author(testAuthor)
                .build();
        testRecipe2 = Recipe.builder()
                .recipeId(2L)
                .recipeName("OMURICE")
                .isValid(false)
                .tags(testTags)
                .ingredients("eggs, milk")
                .recipe("mix together and fry")
                .author(testAuthor)
                .build();

        returnTestRecipeDto1=RecipeDto.builder()
                .recipeId(1L)
                .recipeName("OMLETTE")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("eggs, milk")
                .recipe("mix together and fry")
                .authorId(testAuthor.getAuthorId())
                .authorName(testAuthor.getAuthorName())
                .build();

        updatedRecipeDto = RecipeDto.builder()
                .recipeId(0L)
                .recipeName("Omlette with ham")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("Eggs, milk, ham")
                .recipe("Mix together and fry")
                .authorId(testAuthor.getAuthorId())
                .authorName(testAuthor.getAuthorName())
                .build();


        updatedRecipeDtoResult = RecipeDto.builder()
                .recipeId(1L)
                .recipeName("OMLETTE WITH HAM")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("eggs, milk, ham")
                .recipe("mix together and fry")
                .authorId(testAuthor.getAuthorId())
                .authorName(testAuthor.getAuthorName())
                .build();
    }

    @Test
    void testCreate(){
        when(authorRepository.getByAuthorName(testRecipeDto1.getAuthorName())).thenReturn(Optional.of(testAuthor));
        when(tagRepository.getByTagName(anyString())).thenAnswer(invocation -> {
            String tagName = invocation.getArgument(0);
            return testTags.stream().filter(tag -> tag.getTagName().equals(tagName)).findFirst();
        });
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecipeDto result = recipeService.create(testRecipeDto1);

        assertNotNull(result);
        verify(recipeRepository).save(argThat(recipe -> {
            assertFalse(recipe.isValid(), "Expected recipe.isValid() to be false");
            return true;
        }));
        verify(tagRepository, times(2)).getByTagName(anyString());
    }

    @Test
    void testCreateNoAuthor(){
        when(authorRepository.getByAuthorName(testRecipeDto1.getAuthorName())).thenReturn(Optional.empty());
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            recipeService.create(testRecipeDto1);
        });
        assertNotNull(exception);
        verify(tagRepository, never()).getByTagName(anyString());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void testRead(){
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(testRecipe1));
        RecipeDto result = recipeService.read(1L);
        verify(recipeRepository, atLeastOnce()).findById(1L);
        assertEquals(result,returnTestRecipeDto1);
    }

    @Test
    void testReadNotFound(){
        when(recipeRepository.findById(3L)).thenReturn(Optional.empty());
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {recipeService.read(3L);});
        assertNotNull(exception);
    }

    @Test
    void searchByTags(){
        CollectionDto<String> tags = new CollectionDto<>();
        tags.setCollection(new ArrayList<>(List.of("Test Tag 1", "Test Tag 2")));

        when(tagRepository.getByTagName("TEST TAG 1")).thenReturn(Optional.of(tag1));
        when(tagRepository.getByTagName("TEST TAG 2")).thenReturn(Optional.of(tag2));
        when(recipeRepository.findByTagNames(anyCollection())).thenReturn(new ArrayList<>(List.of(testRecipe1, testRecipe2)));

        Collection<RecipeDto> result = recipeService.searchByTags(tags);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tagRepository, times(2)).getByTagName(anyString());
        verify(recipeRepository).findByTagNames(anyCollection());
    }

    @Test
    void testUpdate(){
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(testRecipe1));
        when(tagRepository.getByTagName("TEST TAG 1")).thenReturn(Optional.of(tag1));
        when(tagRepository.getByTagName("TEST TAG 2")).thenReturn(Optional.of(tag2));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RecipeDto updated=recipeService.update(1L,updatedRecipeDto);
        assertNotNull(updated);
        verify(recipeRepository).save(argThat(recipe -> {
            assertFalse(recipe.isValid(), "Expected recipe.isValid() to be false");
            return true;
        }));
        verify(tagRepository, times(2)).getByTagName(anyString());
        assertEquals(updated,updatedRecipeDtoResult);
    }

    @Test
    void testUpdateNotFound(){
        when(recipeRepository.findById(3L)).thenReturn(Optional.empty());
        NullPointerException exception = assertThrows(NullPointerException.class, ()-> {recipeService.update(3L,updatedRecipeDto);});
        assertNotNull(exception);
        verify(tagRepository, never()).getByTagName(anyString());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void testAddTagById() {
        Long recipeId = 1L;
        Long tagId = 1L;
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag1));
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(testRecipe1));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecipeDto result = recipeService.addTagById(recipeId, tagId);

        assertNotNull(result);
        assertTrue(result.getTags().stream().anyMatch(tag -> tag.getTagId().equals(tagId)), "Tag should be added to the recipe");
    }

    @Test
    void testAddTagByIdInvalidRecipeId() {
        assertThrows(IllegalArgumentException.class, () -> {
            recipeService.addTagById(0L, 1L);
        });
    }

    @Test
    void testAddTagByIdInvalidTagId() {
        assertThrows(IllegalArgumentException.class, () -> {
            recipeService.addTagById(1L, 0L);
        });
    }

    @Test
    void testAddTagByIdTagOrRecipeNotFound() {
        Long recipeId = 1L;
        Long tagId = 1L;
        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            recipeService.addTagById(recipeId, tagId);
        });
    }

    @Test
    void testRemoveTagById(){
        Long recipeId = 1L;
        Long tagId = 1L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(testRecipe1));
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag1));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecipeDto result = recipeService.removeTagById(recipeId,tagId);

        assertNotNull(result);
        assertFalse(result.getTags().stream().anyMatch(tag -> tag.getTagId().equals(tagId)), "Tag should be added to the recipe");
    }

    @Test
    void testRemoveTagByIdInvalidRecipeId(){
        assertThrows(IllegalArgumentException.class, () -> {
            recipeService.removeTagById(0L, 1L);
        });
    }

    @Test
    void testRemoveTagByIdInvalidTagId(){
        assertThrows(IllegalArgumentException.class, () -> {
            recipeService.removeTagById(1L, 0L);
        });
    }

    @Test
    void testRemoveTagByIdTagOrRecipeNotFound() {
        Long recipeId = 1L;
        Long tagId = 1L;
        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            recipeService.removeTagById(recipeId, tagId);
        });

    }

    @Test
    void testDeleteRecipe(){
        Long recipeId = 1L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(testRecipe1));
        recipeService.delete(recipeId);

        verify(recipeRepository, atMostOnce()).delete(any(Recipe.class));
        verify(tagRepository,never()).delete(any(Tag.class));
    }

    @Test
    void testDeleteRecipeNotFound(){
        Long recipeId = 1L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            recipeService.delete(recipeId);
        });


    }
}
