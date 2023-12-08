package com.astindg.movieMatch.model;

import lombok.*;

import javax.persistence.*;
import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"id", "image"})
@Entity
@Table(name = "Movies")
public class Movie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "year_of_release")
    private Integer yearOfRelease;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ua")
    private String nameUa;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "genre_en")
    private String genreEn;

    @Column(name = "genre_ua")
    private String genreUa;

    @Column(name = "genre_ru")
    private String genreRu;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "description_ua")
    private String descriptionUa;

    @Column(name = "description_ru")
    private String descriptionRu;

    @Column(name = "image")
    private File image;

    public Movie(Integer id, String nameEn, Integer yearOfRelease, String genreEn, String descriptionEn, File image) {
        this.id = id;
        this.nameEn = nameEn;
        this.yearOfRelease = yearOfRelease;
        this.genreEn = genreEn;
        this.descriptionEn = descriptionEn;
        this.image = image;
    }
}
