package com.u238.recipeApi.controller;

import com.u238.recipeApi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("tagServiceImpl")
    private TagService tagService;

    void testCreateSuccess(){

    }

    void testCreateConstrainViolation(){

    }

    void testCreateTagExists(){

    }

    void testReadSuccess(){

    }

    void testReadNotFound(){

    }

    void testDeleteSuccess(){

    }

    void testDeleteInvalidArgument(){

    }

    void testDeleteNotFound(){

    }
}
