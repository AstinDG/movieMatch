package com.astindg.movieMatch.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
    @Min(value = 1895, message = "Year of release must be later than 1895")
    @Max(value = 2100, message = "Year of release must be earlier than 2100")
    private Integer yearOfRelease;

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
