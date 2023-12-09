package com.astindg.movieMatch.model;

import java.io.File;

public interface MovieDetails {
    String getName();
    String getDescription();
    File getImage();
    String getGenre();
    Integer getYearOfRelease();
}
