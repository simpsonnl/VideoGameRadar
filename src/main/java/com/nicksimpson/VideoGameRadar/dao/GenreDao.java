package com.nicksimpson.VideoGameRadar.dao;

import com.nicksimpson.VideoGameRadar.model.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> findAll();
    Genre findById(Long id);
    void save(Genre genre);
    void delete(Genre genre);
}
