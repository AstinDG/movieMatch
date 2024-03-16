package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import com.astindg.movieMatch.model.Message;
import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.model.User;
import com.astindg.movieMatch.services.UserService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.*;

@Component
public class CommandHandlerImpl implements CommandHandler {

    private final MessageBuilder messageBuilder;
    private final SessionHandler sessionHandler;
    private final UserService userService;

    private final Queue<Pair<Long, Message>> notifyQueue = new PriorityQueue<>();

    @Autowired
    public CommandHandlerImpl(MessageBuilder messageBuilder, SessionHandler sessionHandler, UserService userService) {
        this.messageBuilder = messageBuilder;
        this.sessionHandler = sessionHandler;
        this.userService = userService;
    }

    public Queue<Pair<Long, Message>> getNotifyQueue() {
        return notifyQueue;
    }

    public Message getReply(User user, Command command) {

        Session session = sessionHandler.getUserSession(user);

        switch (command) {
            case FRIEND_ADD -> {
                //initialise invite code
                sessionHandler.initializeInviteCode(session);
            }
            case MOVIE_MATCH -> {
                session.initializeMovieList();
                sessionHandler.selectRandomMovie(session);
            }
            case MOVIE_LiKE -> {
                sessionHandler.setLikeLastMovieSown(session);
            }
            case MOVIE_DISLIKE -> {
                sessionHandler.setDislikeLastMovieShown(session);
            }
        }
        userService.incrementMessageCounter();
        return command.getAnswer(session, messageBuilder);
    }

    public Message getReply(User user, String text) {
        Session session = sessionHandler.getUserSession(user);
        Message message;

        if (!session.isLookingForFriend()) {
            message = messageBuilder.setLanguage(session.getUser().getLanguage()).withErrorUnknownCommand().withInitialKeyboard().build();
        } else {
            message = addFriendByCode(session, text);
        }

        userService.incrementMessageCounter();
        return message;
    }

    public Message getReplyCallbackQuery(User user, CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();

        Session session = sessionHandler.getUserSession(user);

        Message message;
        if (callbackData.equals("code_process")) {
            session.enableProcessingCode();
            message = messageBuilder.setLanguage(session.getUser().getLanguage()).withEnterInviteCodeText().build();
        } else if (callbackData.startsWith("friend_delete_")) {
            message = deleteFriend(user, callbackData);
        } else if (callbackData.startsWith("friend_")) {
            message = setFriend(user, callbackData);
        } else if (callbackData.startsWith("set_language_")) {
            message = setLanguage(user, callbackData);
        } else if (callbackData.startsWith("show_movie_favorite_list_")) {
            message = editButtons(user, callbackQuery);
        } else if (callbackData.startsWith("show_movie_")) {
            message = showMovieWithRemoveButton(user, callbackData);
        } else if (callbackData.startsWith("remove_movie_")) {
            message = deleteMovie(user, callbackData);
        } else {
            message = messageBuilder.setLanguage(session.getUser().getLanguage())
                    .withErrorUnknownCallback().build();
        }

        userService.incrementMessageCounter();
        return message;
    }

    private Message editButtons(User user, CallbackQuery callback) {
        Optional<Session> session = sessionHandler.findSession(user);
        if (session.isEmpty()) {
            return getReply(user, Command.INITIAL);
        }
        Language usrLang = session.get().getUser().getLanguage();
        int start;
        try {
            start = Integer.parseInt(callback.getData().substring("show_movie_favorite_list_".length()));
        } catch (NumberFormatException ex) {
            return messageBuilder.setLanguage(usrLang).withMovieNotFoundInFavoriteListText().build();
        }

        Message editMessage = messageBuilder.setLanguage(usrLang)
                .withFavoriteMoviesButtons(session.get(), start).build();

        editMessage.setEditMessageId(callback.getMessage().getMessageId());
        editMessage.setHasEditButtons(true);

        return editMessage;
    }

    private Message deleteMovie(User user, String callbackData) {
        String[] data = callbackData.split("_");
        String type = data[2];
        boolean typeIsFavorite = type.equals("favorite");
        String movieIdStr = data[3];

        Optional<Session> session = sessionHandler.findSession(user);
        if (session.isEmpty()) {
            return getReply(user, Command.INITIAL);
        }
        Language usrLang = session.get().getUser().getLanguage();
        MessageBuilder builder = messageBuilder.setLanguage(usrLang);
        int movieId;
        if (typeIsFavorite) {
            try {
                movieId = Integer.parseInt(movieIdStr);
            } catch (NumberFormatException ex) {
                return builder.withMovieNotFoundInFavoriteListText().build();
            }
            Optional<Movie> deletedMovie = this.sessionHandler.deleteFavoriteMovie(movieId, session.get());
            if (deletedMovie.isPresent()) {
                return builder.withMovieDeletedFromFavoriteListText(deletedMovie.get()).build();
            } else {
                return builder.withMovieNotFoundInFavoriteListText().build();
            }
        } else {
            try {
                movieId = Integer.parseInt(movieIdStr);
            } catch (NumberFormatException ex) {
                return builder.withMovieNotFoundInDislikedListText().build();
            }
            Optional<Movie> deletedMovie = this.sessionHandler.deleteDislikedMovie(movieId, session.get());
            if(deletedMovie.isPresent()){
                return builder.withMovieDeletedFromDislikedListText(deletedMovie.get()).build();
            }
        }
        return builder.withMovieNotFoundInFavoriteListText().build();
    }

    private Message showMovieWithRemoveButton(User user, String callbackData) {
        String[] data = callbackData.split("_");
        String type = data[2];
        boolean typeIsFavorite = type.equals("favorite");
        String movieIdStr = data[3];

        Optional<Session> session = sessionHandler.findSession(user);
        if (session.isEmpty()) {
            return getReply(user, Command.INITIAL);
        }
        Language usrLang = session.get().getUser().getLanguage();

        int movieId;
        try {
            movieId = Integer.parseInt(movieIdStr);
        } catch (NumberFormatException ex) {
            if (typeIsFavorite) {
                return messageBuilder.setLanguage(usrLang).withMovieNotFoundInFavoriteListText().build();
            } else {
                return messageBuilder.setLanguage(usrLang).withMovieNotFoundInDislikedListText().build();
            }
        }

        List<Movie> list = typeIsFavorite ?
                session.get().getUser().getFavoriteMovies() : session.get().getUser().getDislikedMovies();

        for (Movie movie : list) {
            if (movie.getId().equals(movieId)) {
                if (typeIsFavorite) {
                    return messageBuilder.setLanguage(usrLang).withMovieMessage(movie).withRemoveFromFavoriteButton(movieId).build();
                } else {
                    return messageBuilder.setLanguage(usrLang).withMovieMessage(movie).withRemoveFromDislikedButton(movieId).build();
                }
            }
        }

        if (typeIsFavorite) {
            return messageBuilder.setLanguage(usrLang).withMovieNotFoundInFavoriteListText().build();
        } else {
            return messageBuilder.setLanguage(usrLang).withMovieNotFoundInDislikedListText().build();
        }
    }

    private Message addFriendByCode(Session session, String code) {

        session.disableProcessingCode();
        // the code contains only digits
        try {
            Long.parseLong(code);
        } catch (NumberFormatException ex) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withIncorrectCodeText().build();
        }
        // the code is not equal to the user's own code and has the correct length
        if (!session.isCorrectLengthCode(code.length()) || code.equals(session.getInviteCode())) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withIncorrectCodeText().build();
        }

        Optional<Session> sessionFriend = sessionHandler.findSessionFriendByCode(code);
        if (sessionFriend.isEmpty()) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).withFriendNotFoundByCodeText(code).build();
        }

        User user = session.getUser();
        User friend = sessionFriend.get().getUser();
        if (user.getFriends() != null) {
            for (User f : user.getFriends()) {
                if (f.equals(friend)) {
                    return messageBuilder.setLanguage(user.getLanguage()).withFriendHasAlreadyBeenAddedText(friend).build();
                }
            }
        } else {
            session.getUser().setFriends(new ArrayList<>());
        }

        if (friend.getFriends() == null) {
            friend.setFriends(new ArrayList<>());
        }

        user.getFriends().add(friend);
        friend.getFriends().add(session.getUser());

        userService.saveFriendByIds(user.getId(), friend.getId());
        userService.saveFriendByIds(friend.getId(), user.getId());

        this.notifyQueue.add(Pair.of(
                        friend.getChatId(),
                        messageBuilder.setLanguage(user.getLanguage()).withNotifyNewFriendText(session).build()
                )
        );

        return messageBuilder.setLanguage(user.getLanguage())
                .withNotifyNewFriendText(sessionFriend.get()).
                withFriendMenuKeyboard().build();

    }

    private Message setFriend(User user, String callbackQuery) {
        Optional<Session> session = sessionHandler.findSession(user);
        if (session.isEmpty()) {
            return getReply(user, Command.INITIAL);
        }

        int index;
        try {
            index = Integer.parseInt(callbackQuery.substring("friend_".length()));
        } catch (NumberFormatException ex) {
            return messageBuilder.setLanguage(user.getLanguage()).withErrorSelectFriendText().build();
        }

        user = session.get().getUser();
        Optional<User> friend = session.get().selectFriend(index);

        if (friend.isEmpty()) {
            return messageBuilder.setLanguage(user.getLanguage()).withErrorSelectFriendText().build();
        }
        return messageBuilder.setLanguage(user.getLanguage())
                .withFriendSelectedText(session.get())
                .appendWithInitialMessage(session.get())
                .withMovieMenuKeyboard().build();
    }

    private Message deleteFriend(User user, String callBackQuery) {
        if (sessionHandler.findSession(user).isEmpty()) {
            return getReply(user, Command.INITIAL);
        } else {
            user = sessionHandler.getUserSession(user).getUser();
        }

        int index;
        try {
            index = Integer.parseInt(callBackQuery.substring("friend_delete_".length()));
        } catch (NumberFormatException ex) {
            return messageBuilder.setLanguage(user.getLanguage()).withErrorDeleteFriendText().build();
        }

        Session sessionUser = sessionHandler.getUserSession(user);
        Optional<User> friend = sessionUser.getFriendByIndex(index);
        if (friend.isEmpty()) {
            return messageBuilder.setLanguage(user.getLanguage()).withErrorDeleteFriendText().build();
        }

        user.getFriends().remove(friend.get());
        friend.get().getFriends().remove(user);

        return messageBuilder.setLanguage(user.getLanguage()).withFriendRemovedText(friend.get()).build();
    }

    private Message setLanguage(User user, String callBackQuery) {
        Optional<Session> session = sessionHandler.findSession(user);
        if (session.isEmpty()) {
            return getReply(user, Command.INITIAL);
        } else {
            user = session.get().getUser();
        }

        Language language;
        try {
            language = Language.valueOf(callBackQuery.substring("set_language_".length()));
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            return messageBuilder.setLanguage(user.getLanguage()).withSelectingLanguageErrorText().build();
        }

        user.setLanguage(language);
        userService.save(user);

        return getReply(user, Command.INITIAL);
    }
}
