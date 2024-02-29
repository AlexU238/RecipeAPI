package com.u238.recipeApi.Dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

@Component
public class CollectionDto<T> implements Dto{

    private Collection<T>collection;

}
