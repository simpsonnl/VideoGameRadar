package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.model.Game;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameService {
    List<Game> findAll();
    Game findById(Long id);
    void save(Game game, MultipartFile file);
    void delete(Game game);
    void toggleFavorite(Game game);
    List<Game> findFavorites();
}
