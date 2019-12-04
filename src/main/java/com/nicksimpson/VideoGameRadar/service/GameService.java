package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.model.Game;
import com.nicksimpson.VideoGameRadar.model.Genre;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameService {
    List<Game> findAll();
    Game findById(Long id);
    void save(Game game, MultipartFile file);
    void delete(Game game);
    void toggleFavorite(Game game);
    List<Game> findFavorites();
    boolean isFavorite(Game game);
    List<Game> listByGenre(String genre);
    List<Game> sortByNameDown(List<Game> games);
    List<Game> sortByNameUp(List<Game> games);
    List<Game> sortByDateNew(List<Game> games);
    List<Game> sortByDateOld(List<Game> games);
}
