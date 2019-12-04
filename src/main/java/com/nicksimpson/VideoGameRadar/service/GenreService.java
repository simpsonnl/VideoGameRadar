package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
    Genre findById(Long id);
    void save(Genre genre);
    void delete(Genre genre);
    void toggleFilter(Genre genre);
}
