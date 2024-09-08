package com.u238.recipeApi.repository;

import com.u238.recipeApi.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {

    Optional<Author> getByAuthorName(String name);

    boolean existsByAuthorName(String name);
}
