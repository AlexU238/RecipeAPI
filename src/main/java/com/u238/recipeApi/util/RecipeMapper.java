package com.u238.recipeApi.util;

import com.u238.recipeApi.Dto.RecipeDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class RecipeMapper implements Mapper<RecipeDto, Recipe> {

    private final TagMapper tagMapper;

    @Autowired
    public RecipeMapper(@Qualifier("tagMapper") TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public RecipeDto toDto(Recipe entity) {
        return RecipeDto.builder()
                .recipeId(entity.getRecipeId())
                .recipeName(entity.getRecipeName())
                .recipe(entity.getRecipe())
                .ingredients(entity.getIngredients())
                .tags(tagMapper.toDtoCollection(entity.getTags()))
                .authorName(entity.getAuthor().getAuthorName())
                .authorId(entity.getAuthor().getAuthorId())
                .build();
    }

    @Override
    public Recipe toEntity(RecipeDto dto) {
        return Recipe.builder()
                .recipeId(dto.getRecipeId())
                .recipeName(dto.getRecipeName().toUpperCase())
                .tags(tagMapper.toEntityCollection(dto.getTags()))
                .ingredients(dto.getIngredients().toLowerCase())
                .recipe(dto.getRecipe().toLowerCase())
                .author(Author.builder().authorId(dto.getAuthorId()).authorName(dto.getAuthorName()).build())
                .build();
    }

    @Override
    public Collection<RecipeDto> toDtoCollection(Collection<Recipe> entityCollection) {
        return entityCollection.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Recipe> toEntityCollection(Collection<RecipeDto> dtoCollection) {
        return dtoCollection.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
