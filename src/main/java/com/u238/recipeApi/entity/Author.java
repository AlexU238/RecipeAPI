package com.u238.recipeApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "author")
public class Author {

    @Id
    //trying out sequence generation type, if needed revert to identity
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long authorId;

    @NaturalId
    @Column(name = "name")
    private String authorName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Collection<Recipe>recipes;
}
