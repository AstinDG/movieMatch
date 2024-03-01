package com.astindg.movieMatch.model;

import lombok.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "movie")
@ToString(exclude = "movie")
@Entity
@Table(name = "Movie_details_en")
public class MovieDetailsEn implements MovieDetails{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @Column(name = "name")
    @Length(min = 1, max = 250, message = "Movie name must be between 1 and 250 characters.")
    private String name;

    @Column(name = "description")
    @Length(min = 1, max = 500, message = "Movie description must be between 1 and 500 characters.")
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    public MovieDetailsEn(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }

    @Override
    public String getGenre(){
        return this.movie.getGenres(Language.EN);
    }

    @Override
    public File getImage() {
        return new File(this.imagePath);
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
            bytes = FileUtils.readFileToByteArray(new File(this.imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (bytes == null) ? "" : Base64.encodeBase64String(bytes);
    }
}
