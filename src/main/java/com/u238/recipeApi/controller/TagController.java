package com.u238.recipeApi.controller;

import com.u238.recipeApi.dto.TagDto;
import com.u238.recipeApi.service.CrudService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

//todo add logging
@RestController
@RequestMapping("/tag")
public class TagController {

    private final CrudService<TagDto> service;

    @Autowired
    public TagController(@Qualifier("tagService") CrudService<TagDto> service) {
        this.service = service;
    }

    @PostMapping
    public TagDto create(@RequestBody TagDto dto) {
        try {
            return service.create(dto);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public TagDto read(@RequestParam Long id) {
        try {
            return service.read(id);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Collection<TagDto> readAll() {
        return service.readAll();
    }

    @PutMapping("/{id}")
    public TagDto update(@RequestParam Long id, @RequestBody TagDto tagDto) {
        try {
            return service.update(id, tagDto);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestParam Long id) {
        try {
            service.delete(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
