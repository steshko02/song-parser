package com.epam.repositories.entity;

import com.epam.model.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArtistRepository extends JpaRepository<Artist,Long> {

    Set<Artist> findByNameStartingWith(String name);


    @Query("select a from Artist a join a.genres g where g.id in :ids and LOWER(a.name)  like LOWER(concat(:name,'%'))")
    List<Artist> getFilters(@Param("ids") Iterable<Long> ids,@Param("name") String name);

    Optional<Artist> findByName(String g);
}
