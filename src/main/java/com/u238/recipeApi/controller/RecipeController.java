package com.u238.recipeApi.controller;

import com.u238.recipeApi.Dto.RecipeDto;
import com.u238.recipeApi.service.RecipeCrudService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeCrudService service;

    @Autowired
    public RecipeController(@Qualifier("recipeService") RecipeCrudService service) {
        this.service = service;
    }

    @PostMapping
    public RecipeDto create(@RequestBody RecipeDto dto){
        try {
            return service.create(dto);
        }catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }catch (IllegalArgumentException | ConstraintViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public RecipeDto read(@RequestParam Long id){
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
                //todo need spring security exception? for this method
                return service.getUnverified();
            }catch (IllegalAccessError e){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

    }

    @PutMapping("/{id}")
    public RecipeDto update(@RequestParam Long id, @RequestBody RecipeDto dto){
        try {
            return service.update(id,dto);
        }catch (IllegalArgumentException | ConstraintViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestParam Long id){
        try {
            service.delete(id);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
