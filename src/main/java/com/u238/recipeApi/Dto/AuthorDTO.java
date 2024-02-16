package com.u238.recipeApi.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class AuthorDTO {

    @NotNull
    private Long authorId;

    @NotBlank
    @Size(max = 255)
    private String authorName;

    @Valid
    private ArrayList<RecipeDTO> recipes;
}
