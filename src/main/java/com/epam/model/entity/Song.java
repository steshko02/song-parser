package com.epam.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name="songs")
@EqualsAndHashCode
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String name;

    private  int year;

    private String notes;

    @ManyToOne (optional=false, cascade = CascadeType.ALL)
    private Album album;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    private  String resourceObjId;

    public Song(){
    }

    public Song(String name, int year, String notes, Album album, Resource resource) {
        this.name = name;
        this.year = year;
        this.notes = notes;
        this.album = album;
        this.resource = resource;
    }

    public Song(Long id, String name, int year, String notes, Album album, Resource resource) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.notes = notes;
        this.album = album;
        this.resource = resource;
    }
}
