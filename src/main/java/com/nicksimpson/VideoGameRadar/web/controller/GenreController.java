package com.nicksimpson.VideoGameRadar.web.controller;

import com.nicksimpson.VideoGameRadar.model.Game;
import com.nicksimpson.VideoGameRadar.model.Genre;
import com.nicksimpson.VideoGameRadar.service.GameService;
import com.nicksimpson.VideoGameRadar.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class GenreController {
    @Autowired
    GenreService genreService;

    @Autowired
    GameService gameService;

    @RequestMapping("/genres")
    public String listGenres(Model model){
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres",genres);
        return "genre/index";
    }

    @RequestMapping("/genres/{genreId}")
    public String genre(@PathVariable Long genreId, Model model){
        Genre genre = genreService.findById(genreId);

        model.addAttribute("genre",genre);
        return "genre/details";
    }


}
