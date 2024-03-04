package com.u238.recipeApi.controller;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.entity.Author;
import com.u238.recipeApi.service.CrudService;
import com.u238.recipeApi.util.Mapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

//todo add logging
@RestController
@RequestMapping("/author")
public class AuthorController {

    private final CrudService<AuthorDto> service;
    private final Mapper<AuthorDto, Author> mapper;

    @Autowired
    public AuthorController(@Qualifier("authorService") CrudService<AuthorDto> service,
                            @Qualifier("authorMapper") Mapper<AuthorDto, Author> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public AuthorDto create(@RequestBody AuthorDto authorDto){
        try {
            return service.create(authorDto);
        }catch (ConstraintViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public AuthorDto read(@PathVariable Long id){
        try {
            return service.read(id);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Collection<AuthorDto> readAll(){
        return service.readAll();
    }

    @PutMapping("/{id}")
    public AuthorDto update(@PathVariable Long id, @RequestBody AuthorDto dto){
        try {
            return service.update(id,dto);
        }catch (ConstraintViolationException | IllegalArgumentException e){
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
}
