package com.u238.recipeApi.controller;


import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.service.AuthorService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("authorServiceImpl")
    private AuthorService authorService;

    private AuthorDto authorDto;

    @BeforeEach
    void setUp(){
        authorDto= AuthorDto.builder().authorId(1L).authorName("Test").build();
    }

    @Test
    void testReadSuccess() throws Exception{
        when(authorService.read(1L)).thenReturn(authorDto);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/author/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(authorDto.getAuthorName())));
    }

    @Test
    void testReadInvalidArgument() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/author/a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReadNotFound() throws Exception{
        when(authorService.read(2L)).thenThrow(NullPointerException.class);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/author/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
