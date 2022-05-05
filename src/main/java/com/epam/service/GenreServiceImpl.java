package com.epam.service;

import com.epam.exceptions.EntityNotFoundException;
import com.epam.model.entity.Genre;
import com.epam.repositories.entity.GenreRepository;
import com.epam.service.interfaces.entity_interface.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Genre create(String name) {
        return new Genre(name);
    }

    @Override
    public Long add(Genre genre) {
        genreRepository.save(genre);
        return genre.getId();
    }

    @Override
    public Long[] delete(Long... ids) {
        for (Long id: ids) {
            genreRepository.deleteById(id);
        }
        return ids;
    }

    @Override
    public List<Genre> getAll() {
       return genreRepository.findAll();
    }

    @Override
    public Genre getById(Long id) {
        Genre genre =genreRepository.findById(id).orElse(null);
        if(genre == null){
            throw new EntityNotFoundException(Genre.class, "id", id.toString());
        }
        return genre;
    }


    private Genre createAndSave(Genre genre){
        genreRepository.save(genre);
        return genre;
    }

    @Override
    @Transactional
    public Set<Genre> getByNameElseSave(List<String> genresNames){

//        Set<Genre> genres = new HashSet<>();
//        genresNames.stream().forEach(g->genres.add(genreRepository.findByName(g).orElse(createAndSave(new Genre(g)))));
//        genresNames.stream().map(g->genreRepository.findByName(g).orElse(createAndSave(new Genre(g)))).collect(Collectors.toSet());

        return genresNames.stream().map(g->genreRepository.findByName(g).orElse(createAndSave(new Genre(g)))).collect(Collectors.toSet());
    }
}
