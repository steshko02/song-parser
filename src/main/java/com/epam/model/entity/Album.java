package com.epam.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String name;

    private  int year;

    private  String notes;

    @ManyToMany
    @JoinTable(name="arist_album", joinColumns=@JoinColumn(name="album_id"),
            inverseJoinColumns=@JoinColumn(name="artist_id"))
    private Set<Artist> artists = new HashSet<>();

    @ManyToMany
    @JoinTable(name="genre_album", joinColumns=@JoinColumn(name="album_id"),
            inverseJoinColumns=@JoinColumn(name="genre_id"))
    private Set<Genre> genres = new HashSet<>();

    public Album(){
    }

    public Album(Long id, String name, int year, String notes) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.notes = notes;
    }

    public Album(String name, int year, String notes) {
        this.name = name;
        this.year = year;
        this.notes = notes;
    }

    public Album(int year, String album) {
        this.name = album;
        this.year = year;
    }

    public Album(Long id, String name, int year, String notes, Set<Artist> artists, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.notes = notes;
        this.artists = artists;
        this.genres = genres;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return year == album.year && Objects.equals(id, album.id) && Objects.equals(name, album.name) && Objects.equals(notes, album.notes) && Objects.equals(artists, album.artists) && Objects.equals(genres, album.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, year, notes, artists, genres);
    }
}
