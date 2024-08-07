package com.u238.recipeApi.controller;

import com.u238.recipeApi.dto.AuthorDto;
import com.u238.recipeApi.service.AuthorService;
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

    private final AuthorService service;

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


}
