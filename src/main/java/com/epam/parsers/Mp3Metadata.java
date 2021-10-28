package com.epam.parsers;

import lombok.Data;

@Data
public class Mp3Metadata {
    private  String name;
    private  int year;
    private String notes;
    private String album;

    public Mp3Metadata(String name, int year, String notes, String album) {
        this.name = name;
        this.year = year;
        this.notes = notes;
        this.album = album;
    }
}
