package com.astindg.movieMatch.model;

import lombok.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "movie")
@ToString(exclude = "movie")
@Entity
@Table(name = "Movie_details_ua")
public class MovieDetailsUa implements MovieDetails{
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

    public MovieDetailsUa(String name, String genre, String description, File image) {
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
    @Override
    public String generateBase64Image() {
        byte[] bytes = null;
        try {
            bytes = FileUtils.readFileToByteArray(this.image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (bytes == null) ? "" : Base64.encodeBase64String(bytes);
    }
}
