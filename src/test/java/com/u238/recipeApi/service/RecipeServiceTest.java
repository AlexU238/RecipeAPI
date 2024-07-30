package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Recipe;
import com.u238.recipeApi.entity.Tag;
import com.u238.recipeApi.repository.AuthorRepository;
import com.u238.recipeApi.repository.RecipeRepository;
import com.u238.recipeApi.repository.TagRepository;
import com.u238.recipeApi.util.RecipeMapper;
import com.u238.recipeApi.util.TagMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private RecipeService recipeService;

    Collection<TagDto>testTagsDto;
    Collection<Tag>testTags;
    RecipeDto testRecipeDto1;
    RecipeDto testRecipeDto2;
    Recipe testRecipe1;
    Recipe testRecipe2;
    RecipeDto updatedRecipeDto;
    Recipe updatedRecipe;
    Author testAuthor;
    TagDto tagDto1;
    TagDto tagDto2;
    Tag tag1;
    Tag tag2;

    @BeforeEach
    void setUp(){
        testTagsDto =new ArrayList<TagDto>();
        tagDto1=TagDto.builder().tagId(1L).tagName("Test tag 1").build();
        testTagsDto.add(tagDto1);
        tagDto2=TagDto.builder().tagId(2L).tagName("Test tag 2").build();
        testTagsDto.add(tagDto2);

        testTags = new ArrayList<Tag>();
        tag1=Tag.builder().tagId(1L).tagName("TEST TAG 1").build();
        testTags.add(tag1);
        tag2=Tag.builder().tagId(2L).tagName("TEST TAG 2").build();
        testTags.add(tag2);

        testAuthor=Author.builder().authorId(0L).authorName("Test").build();

        testRecipeDto1 = RecipeDto.builder()
                .recipeId(1L)
                .recipeName("OMLETTE")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("Eggs, milk")
                .recipe("Mix together and fry")
                .authorId(testAuthor.getAuthorId())
                .authorName(testAuthor.getAuthorName())
                .build();

        testRecipeDto2 = RecipeDto.builder()
                .recipeId(2L)
                .recipeName("Omurice")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("Eggs, milk, rice")
                .recipe("Mix together and fry")
                .authorId(testAuthor.getAuthorId())
                .authorName(testAuthor.getAuthorName())
                .build();

        testRecipe1 = Recipe.builder()
                .recipeId(1L)
                .recipeName("OMLETTE")
                .isValid(false)
                .tags(testTags)
                .ingredients("Eggs, milk")
                .recipe("Mix together and fry")
                .author(testAuthor)
                .build();
        testRecipe2 = Recipe.builder()
                .recipeId(2L)
                .recipeName("OMURICE")
                .isValid(false)
                .tags(testTags)
                .ingredients("Eggs, milk")
                .recipe("Mix together and fry")
                .author(testAuthor)
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

        updatedRecipe = Recipe.builder()
                .recipeId(1L)
                .recipeName("OMLETTE WITH HAM")
                .isValid(false)
                .tags(testTags)
                .ingredients("Eggs, milk, ham")
                .recipe("Mix together and fry")
                .author(testAuthor)
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
        verify(authorRepository).getByAuthorName(testRecipeDto1.getAuthorName());
        verify(tagRepository, times(2)).getByTagName(anyString());
    }

}
