package com.u238.recipeApi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.u238.recipeApi.dto.CollectionDto;
import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.service.RecipeCrudService;
import jakarta.validation.ConstraintViolationException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.contains;
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

    private String asJsonString(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            Assertions.fail("Cannot transform object" + o + "into JSON for the purposes of the test. With exception: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Assertions.fail("Unexpected exception happened while transforming object" + o + "to JSON" + e.getMessage());
            return null;
        }
    }

    private void displayUnexpectedException(Exception e) {
        Assertions.fail("Unexpected exception happened during mockMvc.perform() with message: " + e.getMessage());
    }

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
    void testCreateSuccess() {
        when(recipeService.create(testRecipeDto1)).thenReturn(testRecipeDto1);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/recipe")
                            .content(Objects.requireNonNull(asJsonString(testRecipeDto1)))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.recipeId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName", Matchers.is(testRecipeDto1.getRecipeName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients", Matchers.is(testRecipeDto1.getIngredients().toLowerCase())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(testRecipeDto1.getAuthorName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.tags", hasSize(1)));
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testCreateNoAuthor() {
        when(recipeService.create(testRecipeDto1)).thenThrow(IllegalStateException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                            .content(Objects.requireNonNull(asJsonString(testRecipeDto1)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }

    }

    @Test
    void testCreateInvalidArgument() {
        when(recipeService.create(testRecipeDto1)).thenThrow(IllegalArgumentException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testCreateConstrainsViolation() {
        when(recipeService.create(testRecipeDto1)).thenThrow(ConstraintViolationException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testReadSuccess() {
        when(recipeService.read(1L)).thenReturn(testRecipeDto1);
        try {
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

        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testReadInvalidArgument() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/recipe/a")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testReadNotFound() {
        when(recipeService.read(1L)).thenThrow(NullPointerException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/author/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testReadByTagsSuccess() {
        CollectionDto<String> dto = new CollectionDto<>();
        dto.setCollection(new ArrayList<>());
        dto.getCollection().add("TEST TAG 1");
        Collection<RecipeDto> recipeDtoCollection = new ArrayList<>();
        recipeDtoCollection.add(testRecipeDto1);
        when(recipeService.searchByTags(dto)).thenReturn(recipeDtoCollection);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/recipe/tag")
                            .content(Objects.requireNonNull(asJsonString(dto)))
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
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testUpdateSuccess() {
        testRecipeDto1.setRecipeName(testRecipeDtoUpdate.getRecipeName().toUpperCase());
        when(recipeService.update(1L, testRecipeDtoUpdate)).thenReturn(testRecipeDto1);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/recipe/1")
                            .content(Objects.requireNonNull(asJsonString(testRecipeDtoUpdate)))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.recipeId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName", Matchers.is(testRecipeDto1.getRecipeName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients", Matchers.is(testRecipeDto1.getIngredients().toLowerCase())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(testRecipeDto1.getAuthorName())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.tags", hasSize(1)));
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testUpdateInvalidArgument() {

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/recipe/a")
                            .content(Objects.requireNonNull(asJsonString(testRecipeDtoUpdate)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testUpdateConstrainViolation() {
        when(recipeService.update(1L, testRecipeDtoUpdate)).thenThrow(ConstraintViolationException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/recipe/1")
                            .content(Objects.requireNonNull(asJsonString(testRecipeDtoUpdate)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testUpdateNotFound() {
        when(recipeService.update(1L, testRecipeDtoUpdate)).thenThrow(NullPointerException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/recipe/1")
                            .content(Objects.requireNonNull(asJsonString(testRecipeDtoUpdate)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testDeleteSuccess() {
        doNothing().when(recipeService).delete(1L);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/recipe/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testDeleteInvalidArgument() {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/recipe/a")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testDeleteNotFound() {
        doThrow(NullPointerException.class).when(recipeService).delete(1L);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/recipe/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testAddTagSuccess() {
        TagDto addingTag = TagDto.builder().tagId(2L).tagName("TEST TAG 2").build();
        testRecipeDtoUpdate.setRecipeName(testRecipeDto1.getRecipeName());
        Collection<TagDto> updatedTags = new ArrayList<>();
        updatedTags.add(tagDto1);
        updatedTags.add(addingTag);
        testRecipeDtoUpdate.setTags(updatedTags);
        when(recipeService.addTagById(1L, 2L)).thenReturn(testRecipeDtoUpdate);

        try {
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
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testAddTagNotFound() {
        when(recipeService.addTagById(1L, 2L)).thenThrow(NullPointerException.class);

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/recipe/1/tag/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testAddTagInvalidArgument1() {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/recipe/a/tag/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testAddTagInvalidArgument2() {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/recipe/1/tag/a")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testRemoveTagSuccess() {
        when(recipeService.removeTagById(1L, 2L)).thenReturn(testRecipeDto1);

        try {
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
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testRemoveTagNotFound() {
        when(recipeService.removeTagById(1L, 2L)).thenThrow(NullPointerException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/recipe/1/tag/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testRemoveTagInvalidArgument1() {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/recipe/1/tag/a")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }

    @Test
    void testRemoveTagInvalidArgument2() {
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/recipe/1/tag/a")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            displayUnexpectedException(e);
        }
    }
}
