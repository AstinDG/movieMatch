package com.astindg.movieMatch.repositories;

import com.astindg.movieMatch.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class MovieRepositoryTests {
    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void resetMovies() {
        movieRepository.deleteAll();
    }

    @Test
    void returnsExactCountOfMoviesInDB() {
        // given
        Integer expectedAmount = 5;

        List<Movie> movieList = new ArrayList<>();
        for (int index = 0; index < expectedAmount; index++) {
            movieList.add(new Movie());
        }

        // when
        Integer resultExpectedZero = movieRepository.getMoviesAmount();
        movieRepository.saveAll(movieList);
        Integer result = movieRepository.getMoviesAmount();
        // then

        assertEquals(resultExpectedZero, 0);
        assertEquals(result, expectedAmount);
    }
}
