package com.u238.recipeApi.controller;

import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.service.TagService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("tagServiceImpl")
    private TagService tagService;

    private TagDto tagDto;

    @BeforeEach
    void setUp(){
       tagDto = TagDto.builder().tagId(1L).tagName("TEST TAG").build();
    }

    @Test
    void testCreateSuccess() throws Exception{
        when(tagService.create(tagDto)).thenReturn(tagDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/tag")
                .content(StringUtils.asJsonString(tagDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tagId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tagName", Matchers.is("TEST TAG")));
    }

    @Test
    void testCreateConstrainViolation() throws Exception{
        when(tagService.create(tagDto)).thenThrow(ConstraintViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/tag")
                .content(StringUtils.asJsonString(tagDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTagExists() throws Exception{
        when(tagService.create(tagDto)).thenThrow(IllegalStateException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/tag")
                .content(StringUtils.asJsonString(tagDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void testReadSuccess() throws Exception{
        when(tagService.read(1L)).thenReturn(tagDto);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/tag/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tagId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tagName", Matchers.is("TEST TAG")));
    }

    @Test
    void testReadNotFound() throws Exception{
        when(tagService.read(1L)).thenThrow(NullPointerException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/tag/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testReadInvalidArgument() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/tag/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteSuccess() throws Exception{
        doNothing().when(tagService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tag/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteInvalidArgument() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tag/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteNotFound() throws Exception{
        doThrow(NullPointerException.class).when(tagService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tag/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
