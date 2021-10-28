package com.epam.service.interfaces.entity_interface;

import com.epam.model.entity.Artist;

import java.util.List;

public interface ArtistService {

    Long add(Artist artist);
    Long edit(Artist artist,Long id);
    Artist get(Long id);
    Long[] delete(Long ...id);
    List<Artist> getByFilters(String name, Long[] ids);
}
