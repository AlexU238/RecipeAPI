package com.u238.recipeApi.service;

import com.u238.recipeApi.Dto.CollectionDto;
import com.u238.recipeApi.Dto.RecipeDto;
import com.u238.recipeApi.Dto.TagDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Recipe;
import com.u238.recipeApi.entity.Tag;
import com.u238.recipeApi.repository.AuthorRepository;
import com.u238.recipeApi.repository.RecipeRepository;
import com.u238.recipeApi.repository.TagRepository;
import com.u238.recipeApi.util.AuthorMapper;
import com.u238.recipeApi.util.RecipeMapper;
import com.u238.recipeApi.util.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService implements RecipeCrudService {

    private final RecipeRepository recipeRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final RecipeMapper recipeMapper;

    //todo delete if not needed
    private final TagMapper tagMapper;
    private final AuthorMapper authorMapper;

    @Autowired
    public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository,
                         @Qualifier("authorRepository") AuthorRepository authorRepository,
                         @Qualifier("tagRepository") TagRepository tagRepository,
                         @Qualifier("recipeMapper") RecipeMapper recipeMapper,
                         @Qualifier("tagMapper") TagMapper tagMapper,
                         @Qualifier("authorMapper") AuthorMapper authorMapper) {
        this.recipeRepository = recipeRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.recipeMapper = recipeMapper;
        this.tagMapper = tagMapper;
        this.authorMapper = authorMapper;
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

    //todo controller methods for search
    //todo searchByName make prefix search
    public RecipeDto searchByName(String name) {
        Optional<Recipe> found = recipeRepository.getByRecipeName(name);
        if (found.isPresent()) {
            return recipeMapper.toDto(found.get());
        } else throw new NullPointerException();
    }

    public Collection<RecipeDto> searchBySeveralParameters(CollectionDto<String> tags) {
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

    //todo controller methods to add/remove tag to recipe
    //todo remove get/add by name if needed
    public RecipeDto addTagByName(Long recipeId, String tagName) {
        if (recipeId <= 0) throw new IllegalArgumentException();
        Optional<Tag> tagOptional = tagRepository.getByTagName(tagName);
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (tagOptional.isEmpty() || recipeOptional.isEmpty()) throw new NullPointerException();
        Recipe recipe = recipeOptional.get();
        recipe.getTags().add(tagOptional.get());
        return recipeMapper.toDto(recipeRepository.save(recipe));
    }


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

    public RecipeDto removeTagByName(Long recipeId, String tagName) {
        if (recipeId <= 0) throw new IllegalArgumentException();
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        Optional<Tag> tagOptional = tagRepository.getByTagName(tagName);
        if (recipeOptional.isEmpty() || tagOptional.isEmpty()) throw new NullPointerException();
        Recipe recipe = recipeOptional.get();
        recipe.getTags().remove(tagOptional.get());
        return recipeMapper.toDto(recipeRepository.save(recipe));
    }

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


