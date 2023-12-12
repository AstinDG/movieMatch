package com.astindg.movieMatch.services;

import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findById(Integer id){
        return movieRepository.findById(id);
    }

    public Integer getMoviesAmount(){
        return movieRepository.getMoviesAmount();
    }
}
