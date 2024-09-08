package com.u238.recipeApi.controller;

import com.u238.recipeApi.dto.CollectionDto;
import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.service.RecipeCrudService;
import com.u238.recipeApi.util.StringUtils;
import jakarta.validation.ConstraintViolationException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("recipeServiceImpl")
    private RecipeCrudService recipeService;

    private RecipeDto testRecipeDto1;
    private RecipeDto testRecipeDtoUpdate;
    private TagDto tagDto1;

    @BeforeEach
    void setUp() {
        Collection<TagDto> testTagsDto = new ArrayList<TagDto>();
        tagDto1 = TagDto.builder().tagId(1L).tagName("TEST TAG 1").build();
        testTagsDto.add(tagDto1);
        testRecipeDto1 = RecipeDto.builder()
                .recipeId(1L)
                .recipeName("OMLETTE")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("eggs, milk")
                .recipe("Mix together and fry")
                .authorId(1L)
                .authorName("Test")
                .build();
        testRecipeDtoUpdate = RecipeDto.builder()
                .recipeId(1L)
                .recipeName("OMLETTE With TOMATO")
                .isValid(false)
                .tags(testTagsDto)
                .ingredients("eggs, milk")
                .recipe("Mix together and fry")
                .authorId(1L)
                .authorName("Test")
                .build();
    }


    @Test
    void testCreateSuccess() throws Exception{
        when(recipeService.create(testRecipeDto1)).thenReturn(testRecipeDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe")
                        .content(StringUtils.asJsonString(testRecipeDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName", Matchers.is(testRecipeDto1.getRecipeName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients", Matchers.is(testRecipeDto1.getIngredients().toLowerCase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(testRecipeDto1.getAuthorName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags", hasSize(1)));
    }

    @Test
    void testCreateNoAuthor() throws Exception{
        when(recipeService.create(testRecipeDto1)).thenThrow(IllegalStateException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                        .content(StringUtils.asJsonString(testRecipeDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateInvalidArgument() throws Exception{
        when(recipeService.create(testRecipeDto1)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                        .content(StringUtils.asJsonString(testRecipeDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateConstrainsViolation() throws Exception{
        when(recipeService.create(testRecipeDto1)).thenThrow(ConstraintViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                        .content(StringUtils.asJsonString(testRecipeDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReadSuccess() throws Exception{
        when(recipeService.read(1L)).thenReturn(testRecipeDto1);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName", Matchers.is(testRecipeDto1.getRecipeName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients", Matchers.is(testRecipeDto1.getIngredients().toLowerCase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(testRecipeDto1.getAuthorName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags", hasSize(1)));
    }

    @Test
    void testReadInvalidArgument() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReadNotFound() throws Exception{
        when(recipeService.read(1L)).thenThrow(NullPointerException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/author/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testReadByTagsSuccess() throws Exception{
        CollectionDto<String> dto = new CollectionDto<>();
        dto.setCollection(new ArrayList<>());
        dto.getCollection().add("TEST TAG 1");
        Collection<RecipeDto> recipeDtoCollection = new ArrayList<>();
        recipeDtoCollection.add(testRecipeDto1);
        when(recipeService.searchByTags(dto)).thenReturn(recipeDtoCollection);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recipe/tag")
                        .content(StringUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].recipeId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].recipeName", Matchers.is(testRecipeDto1.getRecipeName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ingredients", Matchers.is(testRecipeDto1.getIngredients().toLowerCase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorName", Matchers.is(testRecipeDto1.getAuthorName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tags", hasSize(1)));
    }

    @Test
    void testUpdateSuccess() throws Exception{
        testRecipeDto1.setRecipeName(testRecipeDtoUpdate.getRecipeName().toUpperCase());
        when(recipeService.update(1L, testRecipeDtoUpdate)).thenReturn(testRecipeDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/recipe/1")
                        .content(StringUtils.asJsonString(testRecipeDtoUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName", Matchers.is(testRecipeDto1.getRecipeName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients", Matchers.is(testRecipeDto1.getIngredients().toLowerCase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(testRecipeDto1.getAuthorName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags", hasSize(1)));
    }

    @Test
    void testUpdateInvalidArgument() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/recipe/a")
                        .content(StringUtils.asJsonString(testRecipeDtoUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateConstrainViolation() throws Exception{
        when(recipeService.update(1L, testRecipeDtoUpdate)).thenThrow(ConstraintViolationException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/recipe/1")
                        .content(StringUtils.asJsonString(testRecipeDtoUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateNotFound() throws Exception{
        when(recipeService.update(1L, testRecipeDtoUpdate)).thenThrow(NullPointerException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/recipe/1")
                        .content(StringUtils.asJsonString(testRecipeDtoUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSuccess() throws Exception{
        doNothing().when(recipeService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/recipe/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteInvalidArgument() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/recipe/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteNotFound() throws Exception{
        doThrow(NullPointerException.class).when(recipeService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/recipe/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddTagSuccess() throws Exception{
        TagDto addingTag = TagDto.builder().tagId(2L).tagName("TEST TAG 2").build();
        testRecipeDtoUpdate.setRecipeName(testRecipeDto1.getRecipeName());
        Collection<TagDto> updatedTags = new ArrayList<>();
        updatedTags.add(tagDto1);
        updatedTags.add(addingTag);
        testRecipeDtoUpdate.setTags(updatedTags);
        when(recipeService.addTagById(1L, 2L)).thenReturn(testRecipeDtoUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe/1/tag/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName", Matchers.is(testRecipeDto1.getRecipeName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients", Matchers.is(testRecipeDto1.getIngredients().toLowerCase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(testRecipeDto1.getAuthorName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags", hasSize(2)));
    }

    @Test
    void testAddTagNotFound() throws Exception{
        when(recipeService.addTagById(1L, 2L)).thenThrow(NullPointerException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe/1/tag/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddTagInvalidArgument1() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe/a/tag/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddTagInvalidArgument2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe/1/tag/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRemoveTagSuccess() throws Exception{
        when(recipeService.removeTagById(1L, 2L)).thenReturn(testRecipeDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/recipe/1/tag/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName", Matchers.is(testRecipeDto1.getRecipeName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients", Matchers.is(testRecipeDto1.getIngredients().toLowerCase())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(testRecipeDto1.getAuthorName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags", hasSize(1)));
    }

    @Test
    void testRemoveTagNotFound() throws Exception{
        when(recipeService.removeTagById(1L, 2L)).thenThrow(NullPointerException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/recipe/1/tag/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveTagInvalidArgument1() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/recipe/1/tag/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRemoveTagInvalidArgument2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/recipe/1/tag/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
