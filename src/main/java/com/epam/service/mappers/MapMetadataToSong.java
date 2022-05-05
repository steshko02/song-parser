package com.epam.service.mappers;

import com.epam.model.entity.Song;
import com.epam.parsers.Mp3Metadata;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class MapMetadataToSong{
    public Song mapping(Mp3Metadata metadata ){
        Song song = new Song();
        song.setName(metadata.getName());
        song.setNotes(metadata.getNotes());
        song.setYear(metadata.getYear());

        return song;
    }

}
