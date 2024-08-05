package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.entity.Recipe;
import com.u238.recipeApi.entity.Tag;
import com.u238.recipeApi.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    TagDto tagDto1;
    Tag tag1;

    @BeforeEach
    void setUp(){
        tagDto1=TagDto.builder().tagId(1L).tagName("TEST TAG 1").build();
        tag1=Tag.builder().tagId(1L).tagName("TEST TAG 1").build();
    }

    @Test
    void testCreate(){
        String tagNameActual="TEST TAG 1";

        when(tagRepository.getByTagName(tagNameActual)).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TagDto result = tagService.create(tagDto1);

        assertNotNull(result);
        verify(tagRepository,times(1)).getByTagName(anyString());
        verify(tagRepository,times(1)).save(any(Tag.class));
        assertEquals(result.getTagName(),tagNameActual);
    }

    @Test
    void testCreateTagExists(){
        String tagNameActual="TEST TAG 1";
        when(tagRepository.getByTagName(tagNameActual)).thenReturn(Optional.of(tag1));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {tagService.create(tagDto1);});
        assertNotNull(exception);
    }
}
