package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.dao.GenreDao;
import com.nicksimpson.VideoGameRadar.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService{
    @Autowired
    private GenreDao genreDao;

    @Override
    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    @Override
    public Genre findById(Long id) {
        return genreDao.findById(id);
    }



    @Override
    public void save(Genre genre) {
        genreDao.save(genre);
    }

    @Override
    public void delete(Genre genre) {
        genreDao.delete(genre);
    }

    @Override
    public void toggleFilter(Genre genre) {
        genre.setFilterCheck(!genre.isFilterCheck());
        genreDao.save(genre);
    }




}
