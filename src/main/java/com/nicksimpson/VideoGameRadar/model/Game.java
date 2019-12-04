package com.nicksimpson.VideoGameRadar.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.Date;


@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] bytes;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Column(nullable = false,columnDefinition = "boolean default true")
    private boolean favorite = false;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    @ManyToOne
    private Genre genre;



    public Game(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }


    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    public static Comparator<Game> GameNameComparatorAscending = new Comparator<Game>() {
        @Override
        public int compare(Game g1, Game g2) {
            String gameName1 = g1.getName().toUpperCase();
            String gameName2 = g2.getName().toUpperCase();


            return gameName1.compareTo(gameName2);
        }
    };

    public static Comparator<Game> GameNameComparatorDescending = new Comparator<Game>() {
        @Override
        public int compare(Game g1, Game g2) {
            String gameName1 = g1.getName().toUpperCase();
            String gameName2 = g2.getName().toUpperCase();


            return gameName2.compareTo(gameName1);
        }
    };

    public static Comparator<Game> GameReleaseDateComparatorOldest = new Comparator<Game>() {
        @Override
        public int compare(Game g1, Game g2) {
            Date date1 = g1.getReleaseDate();
            Date date2 = g2.getReleaseDate();

            if(date1 == null){
                return 1;
            }else if (date2 == null){
                return -1;
            }
            int i = date1.compareTo(date2);
            return date1.compareTo(date2);
        }
    };

    public static Comparator<Game> GameReleaseDateComparatorNewest = new Comparator<Game>() {
        @Override
        public int compare(Game g1, Game g2) {
            Date date1 = g1.getReleaseDate();
            Date date2 = g2.getReleaseDate();

            if(date1 == null){
                return -1;
            }else if (date2 == null){
                return 0;
            }
            return date2.compareTo(date1);
        }
    };

}
