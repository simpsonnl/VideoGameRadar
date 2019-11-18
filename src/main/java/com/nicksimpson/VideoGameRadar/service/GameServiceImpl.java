package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.dao.GameDao;
import com.nicksimpson.VideoGameRadar.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
}
