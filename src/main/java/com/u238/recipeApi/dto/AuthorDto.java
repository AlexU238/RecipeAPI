package com.u238.recipeApi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class AuthorDto implements Dto {

    private Long authorId;

    @NotBlank
    @Size(max = 255)
    private String authorName;

    @JsonIgnore
    private Collection<RecipeDto> recipes;
}
