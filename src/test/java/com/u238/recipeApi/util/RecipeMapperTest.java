package com.u238.recipeApi.util;

import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Recipe;
import com.u238.recipeApi.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeMapperTest {

    Recipe recipe1;
    Collection<Tag> testTags;
    Tag tag1;
    Collection<TagDto>testTagsDto;
    TagDto tagDto1;
    Author testAuthor;
    RecipeDto testRecipeDto1;
    RecipeDto returnTestRecipeDto1;

    @BeforeEach
    void setUp() {
        testTags = new ArrayList<Tag>();
        tag1=Tag.builder().tagId(1L).tagName("TEST TAG 1").build();
        testTags.add(tag1);
        testTagsDto =new ArrayList<TagDto>();
        tagDto1=TagDto.builder().tagId(1L).tagName("TEST TAG 1").build();
        testTagsDto.add(tagDto1);

        testAuthor = Author.builder().authorId(1L).authorName("Test").build();

        recipe1 = Recipe.builder()
                .recipeId(1L)
                .recipeName("TEST1")
                .isValid(false)
                .tags(testTags)
                .ingredients("test")
                .recipe("Test")
                .author(testAuthor)
                .build();

        testRecipeDto1 = RecipeDto.builder()
                .recipeId(1L)
                .recipeName("Test1")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("Test")
                .recipe("Test")
                .authorId(testAuthor.getAuthorId())
                .authorName(testAuthor.getAuthorName())
                .build();

        returnTestRecipeDto1 = RecipeDto.builder()
                .recipeId(testRecipeDto1.getRecipeId())
                .recipeName(testRecipeDto1.getRecipeName().toUpperCase())
                .isValid(testRecipeDto1.isValid())
                .tags(testRecipeDto1.getTags())
                .ingredients(testRecipeDto1.getIngredients().toLowerCase())
                .recipe(testRecipeDto1.getRecipe())
                .authorId(testRecipeDto1.getAuthorId())
                .authorName(testRecipeDto1.getAuthorName())
                .build();
    }


    @Test
    void testToDto(){
        RecipeDto result = RecipeMapper.toDto(recipe1);

        assertNotNull(result);
        assertEquals(result.getRecipeId(),recipe1.getRecipeId());
        assertEquals(result.getRecipeName(),recipe1.getRecipeName());
        assertEquals(result.getRecipe(),recipe1.getRecipe());
        assertEquals(result.getIngredients(),recipe1.getIngredients());
        assertEquals(result.getAuthorId(),recipe1.getAuthor().getAuthorId());
        assertEquals(result.getAuthorName(),recipe1.getAuthor().getAuthorName());
        assertEquals(result.isValid(),recipe1.isValid());
        assertTrue(result.getTags().contains(tagDto1));
    }

    @Test
    void testToEntity(){
        Recipe result = RecipeMapper.toEntity(testRecipeDto1);

        assertNotNull(result);
        assertEquals(result.getRecipeId(),testRecipeDto1.getRecipeId());
        assertEquals(result.getRecipeName(),testRecipeDto1.getRecipeName().toUpperCase());
        assertEquals(result.getRecipe(),testRecipeDto1.getRecipe());
        assertEquals(result.getIngredients(),testRecipeDto1.getIngredients().toLowerCase());
        assertEquals(result.isValid(),testRecipeDto1.isValid());
        assertEquals(result.getAuthor(),Author.builder().authorId(testRecipeDto1.getAuthorId()).authorName(testRecipeDto1.getAuthorName()).build());
        assertTrue(result.getTags().contains(tag1));
    }

    @Test
    void testToDtoCollection(){
        ArrayList<Recipe>recipes=new ArrayList<>();
        recipes.add(recipe1);

        Collection<RecipeDto>result = RecipeMapper.toDtoCollection(recipes);

        assertNotNull(result);
        assertTrue(result.contains(returnTestRecipeDto1));
    }

    @Test
    void testToEntityCollection(){
        ArrayList<RecipeDto>recipeDtos=new ArrayList<>();
        recipeDtos.add(testRecipeDto1);

        Collection<Recipe>result = RecipeMapper.toEntityCollection(recipeDtos);

        assertNotNull(result);
        assertTrue(result.contains(recipe1));
    }

}
