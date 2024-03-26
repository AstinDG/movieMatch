package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.model.User;
import com.astindg.movieMatch.services.MovieService;
import com.astindg.movieMatch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static com.astindg.movieMatch.domain.Session.INVITE_CODE_LENGTH;

@Component
public class SessionHandler {
    private final Map<String, Session> inviteCodes = new HashMap<>();
    private final Map<Long, Session> sessions = new HashMap<>();
    private final MovieService movieService;
    private final UserService userService;

    @Autowired
    public SessionHandler(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    protected Session getUserSession(User user) {
        if (!sessions.containsKey(user.getChatId())) {
            User foundUser = userService.findByChatId(user.getChatId());

            if (foundUser != null) {
                user = foundUser;
            }

            Session session = new Session(movieService);
            session.setUser(user);
            userService.save(user);
            this.sessions.put(user.getChatId(), session);

            return session;
        } else {
            return this.sessions.get(user.getChatId());
        }
    }

    protected void initializeInviteCode(Session session) {
        String code = "";
        while (code.isEmpty()) {
            StringBuilder codeSB = new StringBuilder();
            //generate code
            for (int i = 0; i < INVITE_CODE_LENGTH; i++) {
                int digit = (int) (Math.random() * 10);
                codeSB.append(digit);
            }

            if (inviteCodes.containsKey(codeSB.toString())) {
                if (!inviteCodes.get(codeSB.toString()).isCodeValid()) {
                    //remove from cache non-valid code and generate new one
                    inviteCodes.remove(code);
                }
            } else {
                code = codeSB.toString();
                //return from cycle ony if generated code is unique
            }

        }

        session.setInviteCode(code);
        session.setTimeOfInitCode(LocalDateTime.now());

        this.inviteCodes.put(code, session);
    }

    protected Optional<Session> findSessionFriendByCode(String code) {
        if (inviteCodes.containsKey(code) && inviteCodes.get(code).isCodeValid()) {
            return Optional.of(inviteCodes.get(code));
        } else {
            return Optional.empty();
        }
    }

    protected Optional<Session> findSession(User user) {
        if (this.sessions.containsKey(user.getChatId())) {
            Session session = this.sessions.get(user.getChatId());
            return Optional.of(session);
        } else {
            User foundUser = userService.findByChatId(user.getChatId());

            if (foundUser != null) {
                user = foundUser;
                Session session = new Session(movieService);
                session.setUser(user);
                this.sessions.put(user.getChatId(), session);
                return Optional.of(session);
            }

            return Optional.empty();
        }
    }

    protected void selectRandomMovie(Session session) {
        if (session.getMovieList() != null && !session.getMovieList().isEmpty()) {
            if (session.getCurrentFriend() != null) {
                List<Movie> movieList = session.getMovieList();
                int randomIndex = (int) (Math.random() * movieList.size());

                session.setLastRandomMovie(movieList.get(randomIndex));
            }
        }
    }

    public void setDislikeLastMovieShown(Session session) {
        if (session.getLastRandomMovie() == null) {
            return;
            //TODO trow exception
        }

        User user = session.getUser();

        user.getDislikedMovies().add(session.getLastRandomMovie());

        userService.saveDislikedMovie(user, session.getLastRandomMovie());

        session.releaseLastMovieShown();
    }

    protected void setLikeLastMovieSown(Session session) {
        if (session.getLastRandomMovie() == null) {
            return;
            //TODO trow exception
        }

        User user = session.getUser();

        user.getFavoriteMovies().add(session.getLastRandomMovie());

        userService.saveLikedMovie(user, session.getLastRandomMovie());

        checkMatches(session);

        session.releaseLastMovieShown();

    }

    private void checkMatches(Session session) {
        Optional<Session> friendSessionOp = findSession(session.getCurrentFriend());

        if (friendSessionOp.isEmpty()) {
            return;
        }
        Session friendSession = friendSessionOp.get();
        List<Movie> friendMovies = friendSession.getUser().getFavoriteMovies();


        if (friendMovies.contains(session.getLastRandomMovie())) {
            //notify user
            session.setNewMatchMovie(session.getLastRandomMovie());
            session.getMoviesMatchWithCurrentFriend().add(session.getLastRandomMovie());

            //notify friend if we`re his/her current friend

            if (session.getCurrentFriend().equals(friendSession.getCurrentFriend())) {
                friendSession.setNewMatchMovie(session.getLastRandomMovie());
                friendSession.getMoviesMatchWithCurrentFriend().add(session.getLastRandomMovie());
            }
        }

    }

    protected Optional<Movie> deleteFavoriteMovie(int movieId, Session session){
        Optional<Movie> movie = findMovie(movieId, session.getUser().getFavoriteMovies());
        if(movie.isEmpty()){
            return movie;
        }
        this.userService.deleteFavoriteMovies(session.getUser(), movie.get());
        return movie;
    }

    protected Optional<Movie> deleteDislikedMovie(int movieId, Session session) {
        Optional<Movie> movie = findMovie(movieId, session.getUser().getDislikedMovies());
        if(movie.isEmpty()){
            return movie;
        }
        this.userService.deleteDislikedMovie(session.getUser(), movie.get());
        return movie;
    }

    private Optional<Movie> findMovie(int movieId, List<Movie> list){
        for(Movie movie : list){
            if(movie.getId().equals(movieId)){
                return Optional.of(movie);
            }
        }
        return Optional.empty();
    }
}
