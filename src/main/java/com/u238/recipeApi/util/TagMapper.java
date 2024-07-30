package com.u238.recipeApi.util;

import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public static TagDto toDto(Tag entity) {
        return TagDto.builder().tagId(entity.getTagId()).tagName(entity.getTagName()).build();
    }

    public static Tag toEntity(TagDto dto) {
        return Tag.builder().tagId(dto.getTagId()).tagName(dto.getTagName()).build();
    }

    public static Collection<TagDto> toDtoCollection(Collection<Tag> entityCollection) {
        return entityCollection.stream()
                .map(TagMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Collection<Tag> toEntityCollection(Collection<TagDto> dtoCollection) {
        return dtoCollection.stream()
                .map(TagMapper::toEntity)
                .collect(Collectors.toList());
    }
}
