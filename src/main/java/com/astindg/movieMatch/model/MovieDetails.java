package com.astindg.movieMatch.model;

import java.io.File;

public interface MovieDetails {
    void setMovie(Movie movie);
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String description);
    File getImage();
    String getGenre();
    Integer getYearOfRelease();
    String generateBase64Image();
    String getImagePath();
    void setImagePath(String imagePath);
}
