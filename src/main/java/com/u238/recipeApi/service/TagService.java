package com.u238.recipeApi.service;

import com.u238.recipeApi.Dto.TagDto;
import com.u238.recipeApi.entity.Tag;
import com.u238.recipeApi.repository.TagRepository;
import com.u238.recipeApi.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TagService implements CrudService<TagDto>{

    private final TagRepository repository;
    private final Mapper<TagDto,Tag> mapper;

    @Autowired
    public TagService(@Qualifier("tagRepository") TagRepository repository,@Qualifier("tagMapper") Mapper<TagDto,Tag> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public TagDto create( TagDto dto) {
        dto.setTagName(dto.getTagName().toUpperCase());
        Optional<Tag> tagOptional = repository.getByTagName(dto.getTagName());
        if(tagOptional.isEmpty()){
            return mapper.toDto(repository.save(mapper.toEntity(dto)));
        }
        return null;
    }

    @Override
    public TagDto read(Long id) {
        Optional<Tag> tagOptional = repository.findById(id);
        return tagOptional.map(mapper::toDto).orElse(null);
    }

    @Override
    public Collection<TagDto> readAll() {
        return mapper.toDtoCollection(repository.findAll());
    }

    @Override
    public TagDto update(Long id, TagDto dto) {
        Optional<Tag> tagOptional = repository.findById(id);
        if(tagOptional.isPresent()){
            Tag tag = mapper.toEntity(dto);
            tag.setTagId(tagOptional.get().getTagId());
            return mapper.toDto(repository.save(tag));
        }
        return null;
    }


    @Override
    public void delete(Long id) {
        Optional<Tag>tagOptional=repository.findById(id);
        if(tagOptional.isPresent()) repository.deleteById(id);
    }

    public void deleteByName(String name) {
        Optional<Tag>tagOptional = repository.getByTagName(name.toUpperCase());
        tagOptional.ifPresent(repository::delete);
    }
}
