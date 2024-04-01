package com.astindg.movieMatch.model;

import com.astindg.movieMatch.util.GenreSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.EnumSet;
import java.util.StringJoiner;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Movies")
@TypeDef(name = "genre-set", typeClass = GenreSet.class)
public class Movie {
    private static final String GENRES_SEPARATOR = "/";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "year_of_release")
    @Min(value = 1895, message = "Year of release must be later than 1895")
    @Max(value = 2100, message = "Year of release must be earlier than 2100")
    private Integer yearOfRelease;

    @Column(name = "genres")
    @Type(type = "genre-set", parameters = {
            @org.hibernate.annotations.Parameter(name = "enumClass", value = "com.astindg.movieMatch.model.Genre")
    })
    private EnumSet<Genre> genres;

    @Valid
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private MovieDetailsEn detailsEn;

    @Valid
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private MovieDetailsRu detailsRu;

    @Valid
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private MovieDetailsUa detailsUa;


    public Movie(Integer yearOfRelease, MovieDetailsEn detailsEn, MovieDetailsUa detailsUa, MovieDetailsRu detailsRu) {
        this.yearOfRelease = yearOfRelease;
        this.detailsEn = detailsEn;
        this.detailsUa = detailsUa;
        this.detailsRu = detailsRu;
    }

    public MovieDetails getMovieDetails(Language language) {
        MovieDetails details = null;
        switch (language) {
            case EN -> details = this.detailsEn;
            case UA -> details = this.detailsUa;
            case RU -> details = this.detailsRu;
            //TODO throw exception
        }
        return details;
    }

    public String getGenres(Language language) {
        StringJoiner genresJoiner = new StringJoiner(GENRES_SEPARATOR);
        this.genres.forEach(genre ->
                genresJoiner.add(genre.getName(language))
        );
        return genresJoiner.toString();
    }
}
