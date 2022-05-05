package com.epam.service;

import com.epam.exceptions.EntityNotFoundException;
import com.epam.model.entity.Artist;
import com.epam.model.entity.Genre;
import com.epam.repositories.entity.ArtistRepository;
import com.epam.repositories.entity.GenreRepository;

import com.epam.service.interfaces.entity_interface.ArtistService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Long add(Artist artist) {
        artistRepository.save(artist);
        return artist.getId();
    }

    private boolean checkExist(Set<Genre> genres){
        for (Genre g: genres) {
            if(!genreRepository.existsById(g.getId()))
                return false;
        }
        return true;
    }

    @Override
    public Long edit(Artist artist,Long id) {
        Artist artistForEdit = artistRepository.findById(id).orElse(null);

        if (checkExist(artist.getGenres()) &&
                artistForEdit!=null){
            artistForEdit.setName(artist.getName());
            artistForEdit.setNotes(artist.getNotes());
            artistForEdit.setGenres(artist.getGenres());
            artistRepository.save(artistForEdit);
            return artistForEdit.getId();
        }
        throw new EntityNotFoundException(Artist.class, "id", id.toString());
    }

    @Override
    public Artist get(Long id) {
        Artist artist = artistRepository.findById(id).orElse(null);
        if(artist == null){
            throw new EntityNotFoundException(Artist.class, "id", id.toString());
        }
        return artist;
    }

    @Override
    public Long[] delete(Long... ids) {
        for (Long id: ids) {
            artistRepository.deleteById(id);
        }
        return ids;
    }

    public List<Artist> getByFilters(String name, Long[] ids){
        return artistRepository.getFilters(Arrays.asList(ids), name);
    }

    private Artist createAndSave(Artist artist){
        artistRepository.save(artist);
        return artist;
    }

    @Override
    public Set<Artist> getByNameElseSave(List<String> artistsNames, Set<Genre> genres){
//        Set<Artist> artists = new HashSet<>();
        return  artistsNames.stream().map(g->artistRepository.findByName(g)
                .orElse(createAndSave(new Artist(g,"",genres)))).collect(Collectors.toSet());
    }
}
