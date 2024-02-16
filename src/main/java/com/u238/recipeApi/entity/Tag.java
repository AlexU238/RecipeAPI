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
@Table(name = "tag")
public class Tag {

    @Id
    //trying out sequence generation type, if needed revert to identity
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long tagId;

    @NaturalId
    @Column(name = "name")
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private Collection<Recipe> recipes;
}
