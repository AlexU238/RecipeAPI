package com.u238.recipeApi.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder

@Component
public class RecipeDTO {

    @NotNull
    private Long recipeId;

    @NotBlank
    private String recipeName;

    @Valid
    private ArrayList<TagDTO> tags;

    @NotBlank
    private String ingredients;

    @NotBlank
    private String recipe;

    @NotNull
    private Long authorId;

    @NotBlank
    private String authorName;

}
