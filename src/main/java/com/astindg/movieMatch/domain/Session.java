package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.model.User;
import com.astindg.movieMatch.services.MovieService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Session {
    private User user;
    private User currentFriend;
    private String inviteCode;
    private LocalDateTime timeOfInitCode;
    private List<Movie> movieList;
    private Boolean processingInviteCode = false;
    private List<Movie> moviesMatchWithCurrentFriend;
    private Boolean hasNewMatches = false;
    private Movie newMatchMovie;
    private Movie lastMovieShown;
    private final MovieService movieService;

    private static final Logger log = LoggerFactory.getLogger(Session.class);

    protected static final int INVITE_CODE_LENGTH = 5;
    protected static final int INVITE_CODE_MAX_AGE_IN_MINUTES = 10;

    public Session(MovieService movieService) {
        this.movieService = movieService;
    }

    protected void initializeMovieList() {
        List<Movie> movieList = new ArrayList<>();

        Set<Movie> moviesShownBefore = new HashSet<>();
        if (this.user.getFavoriteMovies() != null) {
            moviesShownBefore.addAll(this.user.getFavoriteMovies());
        }
        if (this.user.getDislikedMovies() != null) {
            moviesShownBefore.addAll(this.user.getDislikedMovies());
        }

        for (Movie movie : movieService.findAll()) {
            if (!moviesShownBefore.contains(movie)) {
                movieList.add(movie);
            }
        }

        this.movieList = movieList;
    }

    protected boolean isCorrectLengthCode(int length) {
        return INVITE_CODE_LENGTH == length;
    }

    protected boolean isLookingForFriend() {
        return this.processingInviteCode;
    }

    protected void enableProcessingCode() {
        this.processingInviteCode = true;
    }

    protected void disableProcessingCode() {
        this.processingInviteCode = false;
    }

    protected int getInviteCodeMaxAge() {
        return INVITE_CODE_MAX_AGE_IN_MINUTES;
    }

    protected Optional<User> selectFriend(int friendId) {
        if (this.user.getFriends() != null) {
            Optional<User> friend = this.user.getFriends().stream().filter(f -> f.getId().equals(friendId)).findFirst();
            if(friend.isPresent()) {
                this.currentFriend = friend.get();
                this.moviesMatchWithCurrentFriend = getNewMatches();
            }
            return friend;
        } else {
            return Optional.empty();
        }
    }

    protected void setNewMatchMovie(Movie movie) {
        this.newMatchMovie = movie;
        this.hasNewMatches = true;
    }

    protected Movie getNewMatchMovie() {
        Movie movie = this.newMatchMovie;

        this.newMatchMovie = null;
        this.hasNewMatches = false;

        return movie;
    }

    protected List<Movie> getNewMatches() {
        List<Movie> movieMatches = new ArrayList<>();

        if (this.user.getFavoriteMovies() != null && this.currentFriend != null) {
            if (this.currentFriend.getFavoriteMovies() != null) {
                List<Movie> movies = this.user.getFavoriteMovies();
                List<Movie> moviesFriend = this.currentFriend.getFavoriteMovies();

                if (!movies.isEmpty() && !moviesFriend.isEmpty()) {
                    movieMatches = movies.stream().filter(moviesFriend::contains)
                            .collect(Collectors.toList());
                }
            }
        }
        return movieMatches;
    }

    protected boolean isCodeValid() {
        long minutesPassed = ChronoUnit.MINUTES.between(this.timeOfInitCode, LocalDateTime.now());
        return INVITE_CODE_MAX_AGE_IN_MINUTES > minutesPassed;
    }

    protected Optional<User> deleteFriendById(int friendId) {
        Optional<User> friend = user.getFriends().stream().filter(f -> f.getId().equals(friendId)).findFirst();
        if(friend.isPresent()){
            user.getFriends().remove(friend.get());
            friend.get().getFriends().remove(user);
        }
        return friend;
    }

    protected void releaseLastMovieShown() {
        movieList.remove(lastMovieShown);
        lastMovieShown = null;
    }
}
