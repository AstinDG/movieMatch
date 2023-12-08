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

            if(foundUser != null){
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

            if(foundUser != null){
                user = foundUser;
                Session session = new Session(movieService);
                session.setUser(user);
                this.sessions.put(user.getChatId(), session);
                return Optional.of(session);
            }

            return Optional.empty();
        }
    }

    protected void selectRandomMovieAndRemoveFromList(Session session) {
        if (session.getMovieList() != null && !session.getMovieList().isEmpty()) {
            if (session.getCurrentFriend() != null) {
                List<Movie> movieList = session.getMovieList();
                int randomIndex = (int) (Math.random() * movieList.size());

                session.setLastMovieShown(movieList.get(randomIndex));

                movieList.remove(randomIndex);
            }
        }
    }

    protected void setLikeLastMovieSown(Session session) {
        if (session.getLastMovieShown() == null) {
            return;
            //TODO trow exception
        }

        User user = session.getUser();
        if (user.getFavoriteMovies() == null) {
            user.setFavoriteMovies(new HashSet<>());
        }

        user.getFavoriteMovies().add(session.getLastMovieShown());
        session.releaseLastMovieShown();
        userService.save(user);
    }

    public void setDislikeLastMovieShown(Session session) {
        if (session.getLastMovieShown() == null) {
            return;
            //TODO trow exception
        }

        User user = session.getUser();
        if (user.getDislikedMovies() == null) {
            user.setDislikedMovies(new HashSet<>());
        }

        user.getDislikedMovies().add(session.getLastMovieShown());
        session.releaseLastMovieShown();
    }

}
