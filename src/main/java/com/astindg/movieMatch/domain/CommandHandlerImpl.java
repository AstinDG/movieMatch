package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.*;
import com.astindg.movieMatch.services.UserService;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.*;

@Component
public class CommandHandlerImpl implements CommandHandler {
    private static final Logger log = LoggerFactory.getLogger(CommandHandlerImpl.class);

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
        log.debug("Command from " + session.getUser() + "- COMMAND:" + command.toString());

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
                sessionHandler.selectRandomMovie(session);
            }
            case MOVIE_DISLIKE -> {
                sessionHandler.setDislikeLastMovieShown(session);
                sessionHandler.selectRandomMovie(session);
            }
        }
        userService.incrementMessageCounter();
        return command.getAnswer(session, messageBuilder);
    }

    public Message getReply(User user, String text) {
        Session session = sessionHandler.getUserSession(user);
        Message message;

        if (!session.isLookingForFriend()) {
            message = messageBuilder.setLanguage(session.getUser().getLanguage()).getText().unknownCommand().getKeyboards().initial().build();
        } else {
            message = addFriendByCode(session, text);
        }

        userService.incrementMessageCounter();
        return message;
    }

    public Message getReplyCallbackQuery(User user, CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();

        Session session = sessionHandler.getUserSession(user);
        user = session.getUser();

        log.debug("Callback from " + session.getUser() + " callbackData=" + callbackData);

        Message message;
        if (callbackData.equals("code_process")) {
            session.enableProcessingCode();
            message = messageBuilder.setLanguage(session.getUser().getLanguage()).getText().inviteCode().build();
        } else if (callbackData.startsWith("friend_delete_")) {
            message = deleteFriend(session, callbackData);
        } else if (callbackData.startsWith("friend_set_")) {
            message = setFriend(user, callbackData);
        } else if (callbackData.startsWith("set_language_")) {
            message = setLanguage(user, callbackData);
        } else if (callbackData.startsWith("show_friend_list_")){
            message = editFriendButtons(session, callbackQuery);
        } else if (callbackData.startsWith("show_movie_favorite_list_") || callbackData.startsWith("show_movie_disliked_list_")) {
            message = editMovieButtons(user, callbackQuery);
        } else if (callbackData.startsWith("show_movie_")) {
            message = showMovieWithRemoveButton(user, callbackData);
        } else if (callbackData.startsWith("remove_movie_")) {
            message = deleteMovie(user, callbackData);
        } else {
            message = messageBuilder.setLanguage(session.getUser().getLanguage())
                    .getText().unknownCallback().build();
        }

        userService.incrementMessageCounter();
        return message;
    }

    private Message editFriendButtons(Session session, CallbackQuery callback){
        String[] data = callback.getData().split("_");
        String friendStartIndex = data[3];

        Language usrLang = session.getUser().getLanguage();
        int start;

        try {
            start = Integer.parseInt(friendStartIndex);
        } catch (NumberFormatException exception) {
            //TODO make method in messageBuilder
            return new Message("Error");
        }

        Message editMessage = messageBuilder.setLanguage(usrLang).getButtons().friendList(session, start).build();
        editMessage.setEditMessageId(callback.getMessage().getMessageId());
        editMessage.setHasEditButtons(true);
        return editMessage;
    }

    private Message editMovieButtons(User user, CallbackQuery callback) {
        String[] data = callback.getData().split("_");
        String type = data[2];
        boolean typeIsFavorite = type.equals("favorite");
        String movieStartIndex = data[4];

        Optional<Session> session = sessionHandler.findSession(user);
        if (session.isEmpty()) {
            return getReply(user, Command.INITIAL);
        }
        Language usrLang = session.get().getUser().getLanguage();
        int start;

        Message editMessage;
        if(typeIsFavorite) {
            try {
                start = Integer.parseInt(movieStartIndex);
            } catch (NumberFormatException ex) {
                return messageBuilder.setLanguage(usrLang).getText().movieNotFoundInFavorite().build();
            }

            editMessage = messageBuilder.setLanguage(usrLang).getButtons().favoriteMovies(session.get(), start).build();
        } else {
            try {
                start = Integer.parseInt(movieStartIndex);
            } catch (NumberFormatException ex) {
                return messageBuilder.setLanguage(usrLang).getText().movieNotFoundInDisliked().build();
            }

            editMessage = messageBuilder.setLanguage(usrLang).getButtons().dislikedMovies(session.get(), start).build();
        }
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
                return builder.getText().movieNotFoundInFavorite().build();
            }
            Optional<Movie> deletedMovie = this.sessionHandler.deleteFavoriteMovie(movieId, session.get());
            if (deletedMovie.isPresent()) {
                return builder.getText().movieDeletedFromFavorite(deletedMovie.get()).build();
            } else {
                return builder.getText().movieNotFoundInFavorite().build();
            }
        } else {
            try {
                movieId = Integer.parseInt(movieIdStr);
            } catch (NumberFormatException ex) {
                return builder.getText().movieNotFoundInDisliked().build();
            }
            Optional<Movie> deletedMovie = this.sessionHandler.deleteDislikedMovie(movieId, session.get());
            if(deletedMovie.isPresent()){
                return builder.getText().movieDeletedFromDisliked(deletedMovie.get()).build();
            }
        }
        return builder.getText().movieNotFoundInFavorite().build();
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
                return messageBuilder.setLanguage(usrLang).getText().movieNotFoundInFavorite().build();
            } else {
                return messageBuilder.setLanguage(usrLang).getText().movieNotFoundInDisliked().build();
            }
        }

        List<Movie> list = typeIsFavorite ?
                session.get().getUser().getFavoriteMovies() : session.get().getUser().getDislikedMovies();

        for (Movie movie : list) {
            if (movie.getId().equals(movieId)) {
                if (typeIsFavorite) {
                    return messageBuilder.setLanguage(usrLang).getMovieMessage(movie).getButtons().removeFromFavorite(movieId).build();
                } else {
                    return messageBuilder.setLanguage(usrLang).getMovieMessage(movie).getButtons().removeFromDisliked(movieId).build();
                }
            }
        }

        if (typeIsFavorite) {
            return messageBuilder.setLanguage(usrLang).getText().movieNotFoundInFavorite().build();
        } else {
            return messageBuilder.setLanguage(usrLang).getText().movieNotFoundInDisliked().build();
        }
    }

    private Message addFriendByCode(Session session, String code) {

        session.disableProcessingCode();
        // the code contains only digits
        try {
            Long.parseLong(code);
        } catch (NumberFormatException ex) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().inviteCodeError().build();
        }
        // the code is not equal to the user's own code and has the correct length
        if (!session.isCorrectLengthCode(code.length()) || code.equals(session.getInviteCode())) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().inviteCodeError().build();
        }

        Optional<Session> sessionFriend = sessionHandler.findSessionFriendByCode(code);
        if (sessionFriend.isEmpty()) {
            return messageBuilder.setLanguage(session.getUser().getLanguage()).getText().friendNotFound(code).build();
        }

        User user = session.getUser();
        User friend = sessionFriend.get().getUser();
        if (user.getFriends() != null) {
            for (User f : user.getFriends()) {
                if (f.equals(friend)) {
                    return messageBuilder.setLanguage(user.getLanguage()).getText().friendAlready(friend).build();
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
                        messageBuilder.setLanguage(user.getLanguage()).getText().notifyNewFriend(session).build()
                )
        );

        return messageBuilder.setLanguage(user.getLanguage())
                .getText().notifyNewFriend(sessionFriend.get()).
                getKeyboards().friends().build();

    }

    private Message setFriend(User user, String callbackData) {
        Optional<Session> session = sessionHandler.findSession(user);
        if (session.isEmpty()) {
            return getReply(user, Command.INITIAL);
        }

        int friendId;
        try {
            friendId = Integer.parseInt(callbackData.substring("friend_set_".length()));
        } catch (NumberFormatException ex) {
            return messageBuilder.setLanguage(user.getLanguage()).getText().friendSelectError().build();
        }

        user = session.get().getUser();
        Optional<User> friend = session.get().selectFriend(friendId);

        if (friend.isEmpty()) {
            return messageBuilder.setLanguage(user.getLanguage()).getText().friendSelectError().build();
        }
        return messageBuilder.setLanguage(user.getLanguage())
                .getText().friendSelected(session.get())
                .getKeyboards().movie().build();
    }

    private Message deleteFriend(Session session, String callbackData) {

        User user = session.getUser();
        int friendId;
        try {
            friendId = Integer.parseInt(callbackData.substring("friend_delete_".length()));
        } catch (NumberFormatException ex) {
            log.warn("Incorrect callback from " + user + " CALLBACKDATA:" + callbackData);
            return messageBuilder.setLanguage(user.getLanguage()).getText().friendDeleteError().build();
        }
        log.debug("Trying to delete friend... " + user + ", friendId=" + friendId);
        Optional<User> friend = sessionHandler.deleteFriend(session, friendId);

        if (friend.isEmpty()) {
            log.debug("Friend not found! " + user + ", friend_id=" + friendId);
            return messageBuilder.setLanguage(user.getLanguage()).getText().friendDeleteError().build();
        }
        log.debug("Friend deleted successfully! " + user + ", friend_id=" + friendId);
        return messageBuilder.setLanguage(user.getLanguage()).getText().friendRemoved(friend.get()).build();
    }

    private Message setLanguage(User user, String callbackData) {
        Language language;
        try {
            language = Language.valueOf(callbackData.substring("set_language_".length()));
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            log.warn("Incorrect callback from " + user + " CALLBACKDATA:\"" + callbackData + "\"");
            return messageBuilder.setLanguage(user.getLanguage()).getText().selectLanguageError().build();
        }
        log.debug("Switch language " + user + " to Lang(" + language + ")");
        user.setLanguage(language);
        userService.saveLanguage(user);
        log.debug("Switch language " + user + "saved");

        return getReply(user, Command.INITIAL);
    }
}