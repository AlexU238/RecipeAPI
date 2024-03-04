package com.u238.recipeApi.service;

import com.u238.recipeApi.dto.CollectionDto;
import com.u238.recipeApi.dto.RecipeDto;

import java.util.Collection;

public interface RecipeCrudService extends CrudService<RecipeDto> {

    Collection<RecipeDto> searchByTags(CollectionDto<String> tags);

    Collection<RecipeDto> getUnverified();


    public RecipeDto addTagById(Long recipeId, Long tagId);


    public RecipeDto removeTagById(Long recipeId, Long tagId);

}
