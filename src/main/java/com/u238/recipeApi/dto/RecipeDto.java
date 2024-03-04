package com.u238.recipeApi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder

@Component
public class RecipeDto implements Dto {

    private Long recipeId;

    @NotBlank
    private String recipeName;

    @NotBlank
    private boolean isValid;

    @Valid
    private Collection<TagDto> tags;

    @NotBlank
    private String ingredients;

    @NotBlank
    private String recipe;

    @NotNull
    private Long authorId;

    @NotBlank
    private String authorName;

}
