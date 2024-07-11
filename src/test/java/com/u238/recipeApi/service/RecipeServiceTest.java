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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private RecipeMapper recipeMapper;
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

    @BeforeEach
    void setUp(){
        testTagsDto =new ArrayList<TagDto>();
        testTagsDto.add(TagDto.builder().tagId(0L).tagName("Test tag 1").build());
        testTagsDto.add(TagDto.builder().tagId(1L).tagName("Test tag 2").build());

        testTags = new ArrayList<Tag>();
        testTags.add(Tag.builder().tagId(1L).tagName("TEST TAG 1").build());
        testTags.add(Tag.builder().tagId(2L).tagName("TEST TAG 2").build());

        testAuthor=Author.builder().authorId(0L).authorName("Test").build();

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
        given(authorRepository.getByAuthorName(testAuthor.getAuthorName())).willReturn(Optional.ofNullable(testAuthor));
        given(recipeMapper.toEntity(testRecipeDto1)).willReturn(testRecipe1);
        given(recipeService.create(testRecipeDto1)).willReturn(testRecipeDto1);

        RecipeDto test = recipeService.create(testRecipeDto1);
        assertNotNull(test);
        assertEquals(testRecipeDto1,test);
        Mockito.verify(recipeRepository,Mockito.atLeastOnce()).save(testRecipe1);
    }
}
