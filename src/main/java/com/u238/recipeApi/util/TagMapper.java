package com.u238.recipeApi.util;

import com.u238.recipeApi.Dto.TagDto;
import com.u238.recipeApi.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class TagMapper implements Mapper<TagDto, Tag> {


    @Override
    public TagDto toDto(Tag entity) {
        return TagDto.builder().tagId(entity.getTagId()).tagName(entity.getTagName()).build();
    }

    @Override
    public Tag toEntity(TagDto dto) {
        return Tag.builder().tagId(dto.getTagId()).tagName(dto.getTagName()).build();
    }

    @Override
    public Collection<TagDto> toDtoCollection(Collection<Tag> entityCollection) {
        return entityCollection.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Tag> toEntityCollection(Collection<TagDto> dtoCollection) {
        return dtoCollection.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
