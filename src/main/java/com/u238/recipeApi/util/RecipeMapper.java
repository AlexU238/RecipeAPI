package com.u238.recipeApi.util;

import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.entity.Recipe;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class RecipeMapper  {


    public static RecipeDto toDto(Recipe entity) {
        return RecipeDto.builder()
                .recipeId(entity.getRecipeId())
                .recipeName(entity.getRecipeName().toUpperCase())
                .recipe(entity.getRecipe())
                .ingredients(entity.getIngredients())
                .tags(TagMapper.toDtoCollection(entity.getTags()))
                .authorName(entity.getAuthor().getAuthorName())
                .authorId(entity.getAuthor().getAuthorId())
                .isValid(entity.isValid())
                .build();
    }

    public static Recipe toEntity(RecipeDto dto) {
        return Recipe.builder()
                .recipeId(dto.getRecipeId())
                .recipeName(dto.getRecipeName().toUpperCase())
                .tags(TagMapper.toEntityCollection(dto.getTags()))
                .ingredients(dto.getIngredients().toLowerCase())
                .recipe(dto.getRecipe().toLowerCase())
                .author(Author.builder().authorId(dto.getAuthorId()).authorName(dto.getAuthorName()).build())
                .isValid(dto.isValid())
                .build();
    }

    public static Collection<RecipeDto> toDtoCollection(Collection<Recipe> entityCollection) {
        return entityCollection.stream()
                .map(RecipeMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Collection<Recipe> toEntityCollection(Collection<RecipeDto> dtoCollection) {
        return dtoCollection.stream()
                .map(RecipeMapper::toEntity)
                .collect(Collectors.toList());
    }
}
