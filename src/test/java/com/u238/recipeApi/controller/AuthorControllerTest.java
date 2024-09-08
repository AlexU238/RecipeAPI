package com.u238.recipeApi.controller;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.service.CrudService;
import com.u238.recipeApi.util.StringUtils;
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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("authorServiceImpl")
    private CrudService<AuthorDto> authorService;

    private AuthorDto authorDto;
    private AuthorDto authorDtoUpd;

    @BeforeEach
    void setUp() {
        authorDto = AuthorDto.builder().authorId(1L).authorName("Test").build();
        authorDtoUpd = AuthorDto.builder().authorId(1L).authorName("Upd").build();
    }

    @Test
    void testReadSuccess() throws Exception {
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
    void testReadInvalidArgument() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/author/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReadNotFound() throws Exception {
        when(authorService.read(2L)).thenThrow(NullPointerException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/author/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateSuccess() throws Exception {
        when(authorService.create(authorDto)).thenReturn(authorDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringUtils.asJsonString(authorDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(authorDto.getAuthorName())));
    }

    @Test
    void testCreateIllegalState() throws Exception{
        when(authorService.create(authorDto)).thenThrow(IllegalStateException.class);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StringUtils.asJsonString(authorDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateIllegalArgument() throws Exception{
        when(authorService.create(authorDto)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringUtils.asJsonString(authorDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateSuccess() throws Exception{
        authorDto.setAuthorName(authorDtoUpd.getAuthorName());
        when(authorService.update(1L,authorDtoUpd)).thenReturn(authorDtoUpd);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringUtils.asJsonString(authorDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Matchers.is(authorDto.getAuthorName())));
    }

    @Test
    void testUpdateIllegalArgument() throws Exception{
        when(authorService.update(1L,authorDtoUpd)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/author/1")
                        .content(StringUtils.asJsonString(authorDtoUpd))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateIllegalState() throws Exception{
        when(authorService.update(1L,authorDtoUpd)).thenThrow(IllegalStateException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/author/1")
                        .content(StringUtils.asJsonString(authorDtoUpd))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateNotFound() throws Exception{
        when(authorService.update(1L,authorDtoUpd)).thenThrow(NullPointerException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringUtils.asJsonString(authorDtoUpd))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSuccess() throws Exception{
        doNothing().when(authorService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/author/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteNotFound() throws Exception{
        doThrow(NullPointerException.class).when(authorService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/author/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
