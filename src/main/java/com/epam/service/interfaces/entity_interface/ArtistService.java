package com.epam.service.interfaces.entity_interface;

import com.epam.model.entity.Artist;
import com.epam.model.entity.Genre;

import java.util.List;
import java.util.Set;

public interface ArtistService {

    Long add(Artist artist);
    Long edit(Artist artist,Long id);
    Artist get(Long id);
    Long[] delete(Long ...id);
    List<Artist> getByFilters(String name, Long[] ids);
    Set<Artist> getByNameElseSave(List<String> artist, Set<Genre> genres);
}
