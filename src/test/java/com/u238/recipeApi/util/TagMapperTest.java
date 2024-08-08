package com.u238.recipeApi.util;

import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class TagMapperTest {

    TagDto tagDto1;
    TagDto returnTagDto1;
    Tag tag1;

    @BeforeEach
    void setUp() {
        tagDto1 = TagDto.builder().tagId(1L).tagName("Test tag 1").build();
        returnTagDto1 = TagDto.builder().tagId(1L).tagName("TEST TAG 1").build();
        tag1 = Tag.builder().tagId(1L).tagName("TEST TAG 1").build();
    }

    @Test
    void testToDto(){
        TagDto result = TagMapper.toDto(tag1);

        assertNotNull(result);
        assertEquals(result.getTagId(),tag1.getTagId());
        assertEquals(result.getTagName(),tag1.getTagName());
    }

    @Test
    void testToEntity(){
        Tag result = TagMapper.toEntity(tagDto1);

        assertNotNull(result);
        assertEquals(result.getTagId(),returnTagDto1.getTagId());
        assertEquals(result.getTagName(),returnTagDto1.getTagName());
    }

    @Test
    void testToDtoCollection(){
        ArrayList<Tag>tags = new ArrayList<>();
        tags.add(tag1);

        Collection<TagDto>tagDtos= TagMapper.toDtoCollection(tags);

        assertNotNull(tagDtos);
        assertTrue(tagDtos.contains(returnTagDto1));
    }

    @Test
    void testToEntityCollection(){
        ArrayList<TagDto>tagDtos = new ArrayList<>();
        tagDtos.add(tagDto1);

        Collection<Tag>tags = TagMapper.toEntityCollection(tagDtos);

        assertNotNull(tags);
        assertTrue(tags.contains(tag1));
    }
}
