package com.astindg.movieMatch.repositories;

import com.astindg.movieMatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByChatId(Long chatId);

    @Modifying
    @Query(value = "insert into Friends (user_id, friend) VALUES (:userId,:friendId)", nativeQuery = true)
    @Transactional
    void saveFriendByIds(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    @Modifying
    @Query(value = "insert into Favorite_movies(user_id, movie_id) VALUES (:userId, :movieId)", nativeQuery = true)
    @Transactional
    void saveFavoriteMovie(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    @Modifying
    @Query(value = "delete from Favorite_movies WHERE user_id=:userId AND movie_id=:movieId", nativeQuery = true)
    @Transactional
    void deleteFavoriteMovie(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    @Modifying
    @Query(value = "delete from Disliked_movies WHERE user_id=:userId AND movie_id=:movieId", nativeQuery = true)
    @Transactional
    void deleteDislikedMovie(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    @Modifying
    @Query(value = "insert into Disliked_movies(user_id, movie_id) VALUES (:userId, :movieId)", nativeQuery = true)
    @Transactional
    void saveDislikedMovie(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    @Query(value = "select count(*) from Users", nativeQuery = true)
    Integer getUsersAmount();
}
