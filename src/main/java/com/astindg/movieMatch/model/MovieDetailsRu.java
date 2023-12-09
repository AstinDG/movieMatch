package com.astindg.movieMatch.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.File;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Movie_details_ru")
@EqualsAndHashCode(exclude = "movie")
public class MovieDetailsRu implements MovieDetails{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @Column(name = "name")
    private String name;

    @Column(name = "genre")
    private String genre;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private File image;

    public MovieDetailsRu(String name, String genre, String description, File image) {
        this.name = name;
        this.genre = genre;
        this.description = description;
        this.image = image;
    }

    @Override
    public Integer getYearOfRelease() {
        if(this.movie == null){
            return 0;
        }
        return this.movie.getYearOfRelease();
    }
}
