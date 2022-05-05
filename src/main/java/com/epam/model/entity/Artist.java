package com.epam.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String name;

    private  String notes;

    @ManyToMany
    @JoinTable(name="artist_genre", joinColumns=@JoinColumn(name="artist_id"),
            inverseJoinColumns=@JoinColumn(name="genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(mappedBy="artists")
    private Set<Album> albums = new HashSet<>();

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }


    public Artist(Long id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    public Artist(String name, String notes, Set<Genre> genres) {
        this.name = name;
        this.notes = notes;
        this.genres = genres;
    }

    public Artist(Long id, String name, String notes, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.genres = genres;
    }
}
