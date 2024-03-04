package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.CollectionDto;
import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Recipe;
import com.u238.recipeApi.entity.Tag;
import com.u238.recipeApi.repository.AuthorRepository;
import com.u238.recipeApi.repository.RecipeRepository;
import com.u238.recipeApi.repository.TagRepository;
import com.u238.recipeApi.util.RecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

//todo add logging

@Service
public class RecipeService implements RecipeCrudService {

    private final RecipeRepository recipeRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final RecipeMapper recipeMapper;


    @Autowired
    public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository,
                         @Qualifier("authorRepository") AuthorRepository authorRepository,
                         @Qualifier("tagRepository") TagRepository tagRepository,
                         @Qualifier("recipeMapper") RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public RecipeDto create(RecipeDto dto) {
        Optional<Author> authorOptional = authorRepository.getByAuthorName(dto.getAuthorName());
        if (authorOptional.isPresent()) {
            //assuming passed security
            dto.setValid(false);
            return recipeMapper.toDto(recipeRepository.save(recipeMapper.toEntity(dto)));
        } else throw new IllegalStateException();

    }

    @Override
    public RecipeDto read(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            return recipeMapper.toDto(recipeOptional.get());
        } else throw new NullPointerException();
    }

    @Override
    public Collection<RecipeDto> readAll() {
        return recipeMapper.toDtoCollection(recipeRepository.findAll().stream().sorted(Comparator.comparing(Recipe::isValid, Comparator.naturalOrder())).collect(Collectors.toList()));
    }

    @Override
    public RecipeDto update(Long id, RecipeDto dto) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeMapper.toEntity(dto);
            recipe.setRecipeId(id);
            return recipeMapper.toDto(recipeRepository.save(recipe));
        } else throw new NullPointerException();
    }

    @Override
    public void delete(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            recipeRepository.delete(recipeOptional.get());
        } else throw new NullPointerException();
    }

    public Collection<RecipeDto> searchByTags(CollectionDto<String> tags) {
        Collection<String> actual = new ArrayList<>();
        for (String tag : tags.getCollection()) {
            Optional<Tag> tagOptional = tagRepository.getByTagName(tag);
            if (tagOptional.isPresent()) {
                actual.add(tag);
            }
        }
        Collection<Recipe> found = recipeRepository.findByTagNames(actual);
        if (found.isEmpty()) return null;
        return recipeMapper.toDtoCollection(found);
    }

    //todo need spring security for this method
    public Collection<RecipeDto> getUnverified() {
        return recipeMapper.toDtoCollection(recipeRepository.getUnverified());
    }

    @Override
    public RecipeDto addTagById(Long recipeId, Long tagId) {
        if (recipeId <= 0) throw new IllegalArgumentException();
        if (tagId <= 0) throw new IllegalArgumentException();
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (tagOptional.isEmpty() || recipeOptional.isEmpty()) throw new NullPointerException();
        Recipe recipe = recipeOptional.get();
        recipe.getTags().add(tagOptional.get());
        return recipeMapper.toDto(recipeRepository.save(recipe));
    }

    @Override
    public RecipeDto removeTagById(Long recipeId, Long tagId) {
        if (recipeId <= 0) throw new IllegalArgumentException();
        if (tagId <= 0) throw new IllegalArgumentException();
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (recipeOptional.isEmpty() || tagOptional.isEmpty()) throw new NullPointerException();
        Recipe recipe = recipeOptional.get();
        Tag tag = tagOptional.get();
        recipe.getTags().remove(tag);
        return recipeMapper.toDto(recipeRepository.save(recipe));
    }

}


