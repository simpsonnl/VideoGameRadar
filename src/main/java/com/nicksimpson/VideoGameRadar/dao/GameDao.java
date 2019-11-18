package com.nicksimpson.VideoGameRadar.dao;

import com.nicksimpson.VideoGameRadar.model.Game;

import java.util.List;

public interface GameDao {
    List<Game> findAll();
    Game findById(Long id);
    void save(Game game);
    void delete(Game game);
}
