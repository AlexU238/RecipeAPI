package com.u238.recipeApi.repository;

import com.u238.recipeApi.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    Optional<Recipe> getByRecipeName(String name);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.tags t WHERE t.tagName IN :tagNames")
    Collection<Recipe> findByTagNames(Collection<String> tagNames);

    @Query("SELECT r FROM Recipe r WHERE r.isValid is FALSE")
    Collection<Recipe> getUnverified();

}
