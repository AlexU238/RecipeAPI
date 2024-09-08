package com.u238.recipeApi.controller;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

//todo add logging
@RequiredArgsConstructor

@RestController
@RequestMapping("/author")
public class AuthorController {

    private final CrudService<AuthorDto> service;

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

    @PostMapping
    public AuthorDto create(@RequestBody AuthorDto dto){
        try{
            return service.create(dto);
        }catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public AuthorDto update(@PathVariable Long id, @RequestBody AuthorDto dto){
        try {
            return service.update(id,dto);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }catch (IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        try {
            service.delete(id);
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
