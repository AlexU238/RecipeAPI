package com.u238.recipeApi.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder

@Component
public class TagDto implements Dto {

    private Long tagId;

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = "^[A-Za-z]+$")
    private String tagName;

    @Valid
    private Collection<RecipeDto> recipes;
}
