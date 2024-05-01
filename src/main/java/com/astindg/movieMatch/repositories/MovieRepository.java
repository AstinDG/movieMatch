package com.astindg.movieMatch.repositories;

import com.astindg.movieMatch.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query(value = "select count(*) from Movies", nativeQuery = true)
    Integer getMoviesAmount();

    @Query(value = "select * from movies where movies.id not in(select favorite_movies.movie_id from favorite_movies where favorite_movies.user_id=:userId) AND movies.id not in (select disliked_movies.movie_id from disliked_movies where disliked_movies.user_id=:userId)", nativeQuery = true)
    List<Movie> getAllNewMoviesByUserId(@Param("userId") Integer userId);
}
