package com.u238.recipeApi.service;

import com.u238.recipeApi.Dto.CollectionDto;
import com.u238.recipeApi.Dto.RecipeDto;

import java.util.Collection;

public interface RecipeCrudService extends CrudService<RecipeDto>{

    public RecipeDto searchByName(String name);

    Collection<RecipeDto> searchBySeveralParameters(CollectionDto<String> tags);

    Collection<RecipeDto> getUnverified();

    public RecipeDto addTagByName(Long recipeId, String tagName);

    public RecipeDto addTagById(Long recipeId, Long tagId);

    public RecipeDto removeTagByName(Long recipeId, String tagName);

    public RecipeDto removeTagById(Long recipeId, Long tagId);

}
