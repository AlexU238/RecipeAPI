package com.u238.recipeApi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder

@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long recipeId;

    @Column(name = "name")
    private String recipeName;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Collection<Tag>tags;

    @Column(name = "ingredients",length = 65535)
    private String ingredients;

    @Column(name = "steps", length = 65535)
    private String recipe;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
