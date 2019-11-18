package com.nicksimpson.VideoGameRadar.web.controller;

import com.nicksimpson.VideoGameRadar.model.Game;
import com.nicksimpson.VideoGameRadar.service.GameService;
import com.nicksimpson.VideoGameRadar.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;


@Controller
public class GameController {
    @Autowired
    private GameService gameService;
    @Autowired
    private GenreService genreService;

    //home
    @RequestMapping("/")
    public String listGames(Model model){
        List<Game> games = gameService.findAll();

        model.addAttribute("games", games);
        model.addAttribute("header","All Games");
        return "game/index";
    }

    //Game image
    @RequestMapping("/games/{gameId}")
    @ResponseBody
    public byte[] gameImgae(@PathVariable Long gameId){
        return gameService.findById(gameId).getBytes();
    }

    //method for saving
    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public String addGame(Game game, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes){
        gameService.save(game, file);

        //TODO: add flash attributes to site

        return String.format("redirect:/");
    }

    //method for deleting
    @RequestMapping(value = "/delete/{gameId}", method = RequestMethod.POST)
    public String deleteGame(@PathVariable Long gameId, RedirectAttributes redirectAttributes){
        Game game = gameService.findById(gameId);
        gameService.delete(game);

        //TODO: add flash attributes to site

        return String.format("redirect:/");
    }

    //method for saving a game edit
    @RequestMapping(value = "/editgames", method = RequestMethod.POST)
    public String addEditedGame(Game game, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes){
        gameService.save(game, file);

        //TODO: add flash attributes to site

        return String.format("redirect:/details/%s",game.getId());
    }

    @RequestMapping("/upload")
    public String uploadGame(Model model){
        if(!model.containsAttribute("game")){
            model.addAttribute("game",new Game());
        }
        model.addAttribute("genres",genreService.findAll());
        model.addAttribute("action","/games");
        model.addAttribute("heading","Upload");
        model.addAttribute("submit","Add");
        return "game/form";
    }

    @RequestMapping("edit/{gameId}")
    public String editGame(@PathVariable Long gameId, Model model){
        //TODO: Make it so that the image is sent to the form

        if(!model.containsAttribute("game")) {
            model.addAttribute("game",gameService.findById(gameId));
        }

        model.addAttribute("picture",gameService.findById(gameId).getBytes());
        model.addAttribute("genres",genreService.findAll());
        model.addAttribute("action","/editgames");
        model.addAttribute("heading","Edit");
        model.addAttribute("submit","Submit");
        return "game/form";
    }


    @RequestMapping("/details/{gameId}")
    public String info(Model model, @PathVariable Long gameId){
        Game game = gameService.findById(gameId);
        model.addAttribute("game",game);
        model.addAttribute("action",String.format("/edit/%s",gameId));
        model.addAttribute("delete",String.format("/delete/%s",gameId));
        return "game/details";
    }


    @RequestMapping("/test")
    public String testing(Model model){
        return "game/test";
    }

    @RequestMapping("/search")
    public String searchResults(@RequestParam String q, Model model){
        List<Game> games = new ArrayList<>();

        for (Game game:gameService.findAll()
             ) {
            String name = game.getName().toString().toLowerCase();
            if(name.contains(q.toLowerCase())){
                games.add(game);
            }
        }
        model.addAttribute("games",games);
        model.addAttribute("header","Search Results");
        return "game/index";
    }

    @RequestMapping("/favorites")
    public String listFavorites(Model model){
        List<Game> games = gameService.findFavorites();

        model.addAttribute("games", games);
        model.addAttribute("header", "Favorites");

        return "game/index";
    }

    @RequestMapping(value = "/games/favorite/{gameId}", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long gameId, Model model, HttpServletRequest request){
        Game game = gameService.findById(gameId);
        gameService.toggleFavorite(game);
        return String.format("redirect:%s",request.getHeader("referer"));
    }
}
