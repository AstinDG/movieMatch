package com.astindg.movieMatch.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Movies")
public class Movie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "year_of_release")
    private Integer yearOfRelease;

    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private MovieDetailsEn detailsEn;

    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private MovieDetailsRu detailsRu;

    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private MovieDetailsUa detailsUa;


    public Movie(Integer yearOfRelease, MovieDetailsEn detailsEn, MovieDetailsUa detailsUa, MovieDetailsRu detailsRu) {
        this.yearOfRelease = yearOfRelease;
        this.detailsEn = detailsEn;
        this.detailsUa = detailsUa;
        this.detailsRu = detailsRu;
    }

    public MovieDetails getMovieDetails(Language language){
        MovieDetails details = null;
        switch (language){
            case EN -> details = this.detailsEn;
            case UA -> details = this.detailsUa;
            case RU -> details = this.detailsRu;
            //TODO throw exception
        }
        return details;
    }
}
