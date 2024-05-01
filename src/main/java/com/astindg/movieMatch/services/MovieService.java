package com.astindg.movieMatch.services;

import com.astindg.movieMatch.model.*;
import com.astindg.movieMatch.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllNewMovies(User user){
        return movieRepository.getAllNewMoviesByUserId(user.getId());
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findById(Integer id) {
        return movieRepository.findById(id);
    }

    @Transactional
    public void saveDetails(Integer id, MovieDetails movieDetails, MultipartFile multipartFile, Language language) {
        Movie movie = findById(id).orElse(null);
        if (movie == null) {
            return;
        }
        MovieDetails actualDetails = movie.getMovieDetails(language);
        checkDescriptionAndSave(movieDetails, actualDetails);
        checkNameAndSave(movieDetails, actualDetails);

        if (multipartFile != null) {
            byte[] newImageBytes = null;
            byte[] currentImageBytes = null;
            try {
                newImageBytes = multipartFile.getBytes();
                currentImageBytes = Files.readAllBytes(actualDetails.getImage().toPath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            if (newImageBytes != null) {
                String path = buildPath(language, multipartFile.getOriginalFilename());
                if (currentImageBytes != null) {
                    if (!Arrays.equals(newImageBytes, currentImageBytes)) {
                        actualDetails.getImage().delete();

                        saveNewImage(actualDetails, path, newImageBytes);
                    }
                } else {
                    saveNewImage(actualDetails, path, newImageBytes);
                }
            }
        }

        this.movieRepository.save(movie);
    }

    private String buildPath(Language language, String fileName) {
        StringBuilder pathSB = new StringBuilder();
        pathSB.append("./src/main/resources/img/movie/");
        switch (language) {
            case EN -> pathSB.append("en/");
            case UA -> pathSB.append("ua/");
            case RU -> pathSB.append("ru/");
        }
        pathSB.append(fileName);
        return pathSB.toString();
    }

    private void saveNewImage(MovieDetails details, String path, byte[] newImageBytes) {
        File newImage = new File(path);

        try (OutputStream os = new FileOutputStream(newImage)) {
            os.write(newImageBytes);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        details.setImagePath(path);
    }

    private void checkDescriptionAndSave(MovieDetails movieDetails, MovieDetails actualDetails) {
        if (!movieDetails.getDescription().isEmpty()) {
            if (!movieDetails.getDescription().equals(actualDetails.getDescription())) {
                actualDetails.setDescription(movieDetails.getDescription());
            }
        }
    }

    private void checkNameAndSave(MovieDetails movieDetails, MovieDetails actualDetails) {
        if (!movieDetails.getName().isEmpty()) {
            if (!movieDetails.getName().equals(actualDetails.getName())) {
                actualDetails.setName(movieDetails.getName());
            }
        }
    }

    public Integer getMoviesAmount() {
        return movieRepository.getMoviesAmount();
    }

    public void updateYearRelease(Integer movieId, Integer updatedYearRelease) {
        Optional<Movie> foundMovie = findById(movieId);
        if (foundMovie.isEmpty()) {
            return;
        }
        Movie movie = foundMovie.get();
        movie.setYearOfRelease(updatedYearRelease);
        this.movieRepository.save(movie);
    }

    public void save(Movie movie) {
        this.movieRepository.save(movie);
    }

    public void save(Movie movie, MultipartFile imageEn, MultipartFile imageUa, MultipartFile imageRu) {
        Arrays.stream(Language.values()).forEach(language ->
                movie.getMovieDetails(language).setMovie(movie)
        );

        byte[] imageEnBytes = null;
        byte[] imageUaBytes = null;
        byte[] imageRuBytes = null;
        try {
            imageEnBytes = imageEn.getBytes();
            imageUaBytes = imageUa.getBytes();
            imageRuBytes = imageRu.getBytes();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (imageEnBytes != null) {
            String path = buildPath(Language.EN, imageEn.getOriginalFilename());
            saveNewImage(movie.getDetailsEn(), path, imageEnBytes);
        }
        if (imageUaBytes != null) {
            String path = buildPath(Language.UA, imageUa.getOriginalFilename());
            saveNewImage(movie.getDetailsUa(), path, imageUaBytes);
        }
        if (imageRuBytes != null) {
            String path = buildPath(Language.RU, imageRu.getOriginalFilename());
            saveNewImage(movie.getDetailsRu(), path, imageRuBytes);
        }
        this.save(movie);
    }

    public void updateGenres(Integer movieId, EnumSet<Genre> genres) {
        Optional<Movie> foundMovie = findById(movieId);
        if (foundMovie.isEmpty()) {
            return;
        }
        Movie movie = foundMovie.get();
        movie.setGenres(genres);
        this.movieRepository.save(movie);
    }
}
