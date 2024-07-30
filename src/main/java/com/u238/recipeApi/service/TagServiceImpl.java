package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.entity.Tag;
import com.u238.recipeApi.repository.TagRepository;
import com.u238.recipeApi.util.Mapper;
import com.u238.recipeApi.util.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

//todo add logging
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    @Autowired
    public TagServiceImpl(@Qualifier("tagRepository") TagRepository repository) {
        this.repository = repository;
    }

    @Override
    public TagDto create( TagDto dto) {
        dto.setTagName(dto.getTagName().toUpperCase());
        Optional<Tag> tagOptional = repository.getByTagName(dto.getTagName());
        if(tagOptional.isEmpty()){
            return TagMapper.toDto(repository.save(TagMapper.toEntity(dto)));
        }else throw new IllegalStateException();
    }

    @Override
    public TagDto read(Long id) {
        Optional<Tag> tagOptional = repository.findById(id);
        if(tagOptional.isPresent()){
            return TagMapper.toDto(tagOptional.get());
        }else throw new NullPointerException();
    }

    @Override
    public Collection<TagDto> readAll() {
        return TagMapper.toDtoCollection(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        if(id<=0) throw new IllegalArgumentException();
        Optional<Tag>tagOptional=repository.findById(id);
        if(tagOptional.isPresent()) {
            repository.deleteById(id);
        }else throw new NullPointerException();
    }

}
