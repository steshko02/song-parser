package com.epam.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String name;

    public Genre() {
    }
    public Genre(String name){
        this.name=name;
    }

    @ManyToMany(mappedBy = "genres")
    private Set<Album> albums = new HashSet<>();

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(String name, Set<Album> albums) {
        this.name = name;
        this.albums = albums;
    }

}
