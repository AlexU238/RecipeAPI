package com.u238.recipeApi.controller;

import com.u238.recipeApi.dto.CollectionDto;
import com.u238.recipeApi.dto.RecipeDto;
import com.u238.recipeApi.service.RecipeCrudService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

//todo add logging

@RestController
@RequestMapping("/recipe")
public class RecipeController{

    private final RecipeCrudService service;

    @Autowired
    public RecipeController(@Qualifier("recipeService") RecipeCrudService service) {
        this.service = service;
    }

    @PostMapping
    public RecipeDto create(@RequestBody RecipeDto dto){
        try {
            System.out.println("Received request");
            return service.create(dto);
        }catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }catch (IllegalArgumentException | ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public RecipeDto read(@PathVariable Long id){
        try {
            return service.read(id);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping
    public Collection<RecipeDto> readAll(@RequestParam(name = "verified", defaultValue = "true") boolean verified){
        if(verified){
            return service.readAll();
        }else {
            try {
                return service.getUnverified();
            }catch (IllegalAccessError e){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
    }

    @GetMapping("/tag")
    public Collection<RecipeDto>readBySeveral(@RequestBody CollectionDto<String> dto){
        return service.searchByTags(dto);
    }

    @PutMapping("/{id}")
    public RecipeDto update(@PathVariable Long id, @RequestBody RecipeDto dto){
        try {
            return service.update(id,dto);
        }catch (IllegalArgumentException | ConstraintViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        try {
            service.delete(id);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{recipeID}/tag/{tagID}")
    public RecipeDto addTag(@PathVariable Long recipeID, @PathVariable Long tagID){
        try {
            return service.addTagById(recipeID,tagID);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{recipeID}/tag/{tagID}")
    public RecipeDto removeTag(@PathVariable Long recipeID, @PathVariable Long tagID){
        try {
            return service.removeTagById(recipeID,tagID);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
