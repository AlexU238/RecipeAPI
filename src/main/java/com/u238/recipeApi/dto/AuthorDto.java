package com.u238.recipeApi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class AuthorDto implements Dto {

    private Long authorId;

    @NotBlank
    @Size(max = 255)
    private String authorName;

    @Valid
    private ArrayList<RecipeDto> recipes;
}
