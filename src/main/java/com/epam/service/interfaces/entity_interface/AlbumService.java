package com.epam.service.interfaces.entity_interface;


import com.epam.model.entity.Album;

public interface AlbumService {

    Long add(Album artist);
    Long edit(Album artist,Long id);
    Album get(Long id);
    Long[] delete(Long ...id);
    Album findByName(String name);
}
