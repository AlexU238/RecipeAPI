package com.u238.recipeApi.controller;

import com.u238.recipeApi.service.RecipeCrudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("recipeServiceImpl")
    private RecipeCrudService recipeService;

    @Test
    void testCreateSuccess(){

    }

    @Test
    void testCreateNoAuthor(){

    }

    @Test
    void testCreateInvalidArgument(){

    }

    @Test
    void testCreateConstrainsViolation(){

    }

    @Test
    void testReadSuccess(){

    }

    @Test
    void testReadInvalidArgument(){

    }

    @Test
    void testReadNotFound(){

    }

    @Test
    void testReadByTagsSuccess(){

    }

    @Test
    void testUpdateSuccess(){

    }

    @Test
    void testUpdateInvalidArgument(){

    }

    @Test
    void testUpdateConstrainViolation(){

    }

    @Test
    void testUpdateNotFound(){

    }

    @Test
    void testDeleteSuccess(){

    }

    @Test
    void testDeleteInvalidArgument(){

    }

    @Test
    void testDeleteNotFound(){

    }

    @Test
    void testAddTagSuccess(){

    }

    @Test
    void testAddTagNotFound(){

    }

    @Test
    void testAddTagInvalidArgument(){

    }

    @Test
    void testRemoveTagSuccess(){

    }

    @Test
    void testRemoveTagNotFound(){

    }

    @Test
    void testRemoveTagInvalidArgument(){

    }
}
