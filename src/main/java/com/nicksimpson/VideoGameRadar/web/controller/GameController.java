package com.nicksimpson.VideoGameRadar.web.controller;

import com.nicksimpson.VideoGameRadar.model.Game;
import com.nicksimpson.VideoGameRadar.model.Genre;
import com.nicksimpson.VideoGameRadar.model.Sort;
import com.nicksimpson.VideoGameRadar.service.GameService;
import com.nicksimpson.VideoGameRadar.service.GenreService;
import com.nicksimpson.VideoGameRadar.service.SortService;
import com.nicksimpson.VideoGameRadar.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;


@Controller
public class GameController {
    @Autowired
    private GameService gameService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private SortService sortService;

    private boolean isSearched = false;
    private boolean isFavorites = false;
    private boolean isFiltered = false;
    private List<Game> favorites = new ArrayList<>();
    private List<Game> search = new ArrayList<>();
    private List<Game> filteredGames = new ArrayList<>();

    private int gameCount = 0;


    //home
    @RequestMapping("/")
    public String listGames(Model model){
        List<Game> games = gameService.findAll();
        List<Genre> genres = genreService.findAll();
        List<Sort> sortOptions = sortService.findAll();

        //resets the filters
        for (Genre genre: genres
        ) {
            genre.setFilterCheck(false);
            genreService.save(genre);
        }

        favorites.clear();
        search.clear();
        filteredGames.clear();
        isFavorites = false;
        isSearched = false;
        isFiltered = false;

        gameCount = games.size();

        games = gameService.sortByNameUp(games);

        model.addAttribute("sortOptions", sortOptions);
        model.addAttribute("count", gameCount);
        model.addAttribute("genres",genres);
        model.addAttribute("games", games);
        model.addAttribute("header","Games");

        return "game/index";
    }

    //Retrieving game image
    @RequestMapping("/games/{gameId}")
    @ResponseBody
    public byte[] gameImgae(@PathVariable Long gameId){
        return gameService.findById(gameId).getBytes();
    }

    //method for saving a game to the database
    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public String addGame(Game game, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        if(game.getName().length() > 50 || game.getName().length() < 3) {
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Name must be between 3 and 50 characters.", FlashMessage.Status.FAILURE));
            return String.format("redirect:%s",request.getHeader("referer"));
        }

        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Please add a picture!", FlashMessage.Status.FAILURE));
            return String.format("redirect:%s",request.getHeader("referer"));
        }else {
            gameService.save(game, file);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Game Added Successfully!", FlashMessage.Status.SUCCESS));
            return String.format("redirect:/");
        }
    }

    //method for deleting
    @RequestMapping(value = "/delete/{gameId}", method = RequestMethod.POST)
    public String deleteGame(@PathVariable Long gameId, RedirectAttributes redirectAttributes){
        Game game = gameService.findById(gameId);
        gameService.delete(game);

        //TODO: add flash errors
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Game Deleted Successfully!", FlashMessage.Status.SUCCESS));


        return String.format("redirect:/");
    }

    //method for saving a game edit
    @RequestMapping(value = "/editgames", method = RequestMethod.POST)
    public String addEditedGame(Game game, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){
        if(game.getName().length() > 50) {
            redirectAttributes.addFlashAttribute("flash",new FlashMessage("Name must be between 3 and 50 characters.", FlashMessage.Status.FAILURE));
            return String.format("redirect:%s",request.getHeader("referer"));
        }

        gameService.save(game, file);

        
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Game Updated Successfully!", FlashMessage.Status.SUCCESS));

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


        if(!model.containsAttribute("game")) {
            model.addAttribute("game",gameService.findById(gameId));
        }

        model.addAttribute("genres",genreService.findAll());
        model.addAttribute("action","/editgames");
        model.addAttribute("heading",String.format("Edit %s", gameService.findById(gameId).getName()));
        model.addAttribute("submit","Submit");
        return "game/form";
    }


    @RequestMapping("/details/{gameId}")
    public String info(Model model, @PathVariable Long gameId){
        Game game = gameService.findById(gameId);
        model.addAttribute("game",game);
        model.addAttribute("action",String.format("/edit/%s",gameId));
        model.addAttribute("delete",String.format("/delete/%s",gameId));
        model.addAttribute("heading","Game Details");
        return "game/details";
    }

    @RequestMapping("/search")
    public String searchResults(@RequestParam String q, Model model){
        List<Game> games = new ArrayList<>();
        List<Sort> sortOptions = sortService.findAll();


        for (Game game:gameService.findAll()
             ) {
            String name = game.getName().toLowerCase();
            if(name.contains(q.toLowerCase())){
                games.add(game);
            }
        }
        List<Genre> genres = genreService.findAll();
        for (Genre genre: genres
        ) {
            genre.setFilterCheck(false);
            genreService.save(genre);
        }

        //clears and sets the search object
        search.clear();
        search.addAll(games);
        isSearched = true;
        gameCount = games.size();

        games = gameService.sortByNameUp(games);

        model.addAttribute("count", gameCount);
        model.addAttribute("sortOptions", sortOptions);
        model.addAttribute("genres",genres);
        model.addAttribute("games",games);
        model.addAttribute("header","Search Results");
        return "game/index";
    }

    //lists all the games where game.favorite is true
    @RequestMapping("/favorites")
    public String listFavorites(Model model){
        List<Genre> genres = genreService.findAll();
        List<Game> games = gameService.findFavorites();
        List<Sort> sortOptions = sortService.findAll();
        //resets the search fields
        search.clear();
        isSearched=false;

        favorites.clear();
        favorites.addAll(games);
        isFavorites = true;

        gameCount = games.size();

        for (Genre genre: genres
        ) {
            genre.setFilterCheck(false);
            genreService.save(genre);
        }

        games = gameService.sortByNameUp(games);

        model.addAttribute("count", gameCount);



        model.addAttribute("sortOptions", sortOptions);
        model.addAttribute("genres",genres);
        model.addAttribute("games", games);
        model.addAttribute("header", "Favorites");

        return "game/index";
    }

    //toggles the favorite field for the given game
    //returns to the page it was on
    @RequestMapping(value = "/games/favorite/{gameId}", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long gameId, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        Game game = gameService.findById(gameId);
        gameService.toggleFavorite(game);

        //displays the appropriate flash message for toggling as a favorite
        if(gameService.isFavorite(game)) {
            redirectAttributes.addFlashAttribute("flash",
                new FlashMessage("Game Added to Favorites!", FlashMessage.Status.SUCCESS));
        }else {
            redirectAttributes.addFlashAttribute("flash",
                new FlashMessage("Game Removed From Favorites!", FlashMessage.Status.SUCCESS));
        }

        //returns you to the referer which is either the index or the favorites page
        return String.format("redirect:%s",request.getHeader("referer"));
    }

    //sets the filters array from whatever is checked on the page
    @RequestMapping(value="/filter/{genreId}", method = RequestMethod.POST)
    public String filterGames(Model model, @PathVariable Long genreId, @RequestParam(value="name", required = false)String[] genreArray){
        genreService.toggleFilter(genreService.findById(genreId));

        //resets the filtered games field
        filteredGames.clear();

        StringBuilder genreNames= new StringBuilder();

        List<Game> games = new ArrayList<>();

        List<Genre> filteredGenres = genreService.findAll();

        List<Sort> sortOptions = sortService.findAll();

        //loops through every genre
        for (Genre genre: filteredGenres
        ) {
            //checks if the given genre is checked or not
            if(genre.isFilterCheck()){
                //if the search boolean has been set to true by the search method
                if(isSearched){
                    //lists every game in the given genre
                    for (Game game: gameService.listByGenre(genre.getName())
                         ) {
                        //if search list contains the given game in the genre
                        //add to the games list
                        for (Game searchedGame: search
                             ) {
                            if(searchedGame.getName().equals(game.getName())) {
                                games.add(game);
                            }
                        }

                    }
                 //search is false
                }else if(isFavorites){
                    //lists every game in the given genre
                    for (Game game: gameService.listByGenre(genre.getName())
                    ) {
                        //if favorites list contains the given game in the genre
                        //add to the games list
                        for (Game favoritedGame: favorites
                        ) {
                            if(favoritedGame.getName().equals(game.getName())) {
                                games.add(game);
                            }
                        }

                    }
                }else{
                    List<Game> tempGames = gameService.listByGenre(genre.getName());
                    //TODO: fix this part for sorting when filtering - figure hout how you want it to work
                    //tempGames = gameService.sortByNameUp(tempGames);
                    games.addAll(tempGames);
                }

                if (!genreNames.toString().equals("")){
                    genreNames.append(String.format(" & %s", genre.getName()));
                }else{
                    genreNames.append(genre.getName());
                }

            }
        }
        //sets the proper list of games if there is nothing checked
        if(genreNames.toString().equals("")){
            if(isSearched){
                games=search;
            }else if(isFavorites){
                games = favorites;
            }else{
                games=gameService.findAll();
            }
        }
        //sorts by name
        games = gameService.sortByNameUp(games);
        //ads the games to the filtered
        filteredGames.addAll(games);

        List<Genre> genres = genreService.findAll();

        if (isSearched) {
            model.addAttribute("header","Search Results");

        }else if(isFavorites){
            model.addAttribute("header", "Favorites");
        }else{
            model.addAttribute("header","Games");
        }

        gameCount = games.size();
        isFiltered = true;

        model.addAttribute("count", gameCount);
        model.addAttribute("sortOptions", sortOptions);
        model.addAttribute("genres",genres);
        model.addAttribute("games",games);

        return "game/index";
    }

    //clears the filter array list
    @RequestMapping("/filters/clear")
    public String clearFilters(){
        List<Genre> genres = genreService.findAll();
        for (Genre genre: genres
             ) {
            genre.setFilterCheck(false);
            genreService.save(genre);
        }
        return String.format("redirect:/");
    }

    @RequestMapping(value="/sort", method = RequestMethod.POST)
    public String sortGames(Model model, HttpServletRequest request) {
        String header;
        String sortOption = request.getParameter("sortOption");


        List<Game> games = new ArrayList<>();

        if(isFavorites){
            header = "Favorites";
            if(isFiltered){
                games.addAll(filteredGames);
            }else {
                games.addAll(favorites);
            }
        }else if(isSearched){
            header = "Search Results";
            if(isFiltered){
                games.addAll(filteredGames);
            }else {
                games.addAll(search);
            }
        }else{
            header = "Games";
            if(isFiltered){
                games.addAll(filteredGames);
            }else {
                games = gameService.findAll();
            }
        }


        List<Genre> genres = genreService.findAll();
        List<Sort> sortOptions = sortService.findAll();




        //reset the search and favorite fields
//        favorites.clear();
//        search.clear();
//        isFavorites = false;
//        isSearched = false;

        gameCount = games.size();


        //sets the sort
        if(sortOption.equals("Name - Ascending")){
            games = gameService.sortByNameUp(games);
        }else if(sortOption.equals("Name - Descending")){
            games = gameService.sortByNameDown(games);
        }else if(sortOption.equals("Date - Newest")){
            games = gameService.sortByDateNew(games);
        }else if(sortOption.equals("Date - Oldest")){
            games = gameService.sortByDateOld(games);
        }else{
            games = gameService.sortByNameUp(games);
        }


        model.addAttribute("sortOption", sortOption);
        model.addAttribute("sortOptions", sortOptions);
        model.addAttribute("count", gameCount);
        model.addAttribute("genres",genres);
        model.addAttribute("games", games);
        model.addAttribute("header",header);
        return "game/index";
    }




}
