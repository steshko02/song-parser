package com.epam.service.interfaces.entity_interface;


import com.epam.model.entity.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {

    Genre create(String name);

    Long add(Genre genre);

    Long[] delete(Long ...id);

    List<Genre> getAll();


    Genre getById(Long id);

    Set<Genre> getByNameElseSave(List<String> genresNames);
}
