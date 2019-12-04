package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.dao.GameDao;
import com.nicksimpson.VideoGameRadar.model.Game;
import com.nicksimpson.VideoGameRadar.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameServiceImpl implements GameService{
    @Autowired
    private GameDao gameDao;

    @Override
    public List<Game> findAll() {
        return gameDao.findAll();
    }

    @Override
    public Game findById(Long id) {
        return gameDao.findById(id);
    }

    @Override
    public void save(Game game, MultipartFile file) {
        try {
            game.setBytes(file.getBytes());
            gameDao.save(game);
        } catch (IOException e) {
            System.err.println("Unable to get byte array for file");
        }

    }

    @Override
    public void delete(Game game) {
        gameDao.delete(game);
    }

    @Override
    public void toggleFavorite(Game game) {
        game.setFavorite(!game.isFavorite());
        gameDao.save(game);
    }

    @Override
    public List<Game> findFavorites() {
        List<Game> games = new ArrayList<>();
        for (Game game:gameDao.findAll()
             ) {
            if(game.isFavorite()){
                games.add(game);
            }
        }
        return games;
    }

    @Override
    public boolean isFavorite(Game game) {
        return game.isFavorite();
    }

    @Override
    public List<Game> listByGenre(String genre) {
        List<Game> games = new ArrayList<>();
        for (Game game:gameDao.findAll()
        ) {
            if(game.getGenre().getName().equals(genre)){
                games.add(game);
            }
        }
        return games;
    }

    @Override
    public List<Game> sortByNameDown(List<Game> games) {
        Collections.sort(games, Game.GameNameComparatorDescending);

        return games;


    }

    @Override
    public List<Game> sortByNameUp(List<Game> games) {
        Collections.sort(games, Game.GameNameComparatorAscending);

        return games;
    }

    @Override
    public List<Game> sortByDateNew(List<Game> games) {
        Collections.sort(games, Game.GameReleaseDateComparatorNewest);

        return games;
    }

    @Override
    public List<Game> sortByDateOld(List<Game> games) {
        Collections.sort(games, Game.GameReleaseDateComparatorOldest);

        return games;
    }
}
