package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import com.astindg.movieMatch.model.Message;
import com.astindg.movieMatch.model.Movie;
import com.astindg.movieMatch.model.User;
import com.astindg.movieMatch.services.UserService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            message =  messageBuilder.setLanguage(session.getUser().getLanguage()).withErrorUnknownCommand().withInitialKeyboard().build();
        } else {
            message = addFriendByCode(session, text);
        }

        userService.incrementMessageCounter();
        return message;
    }

    public Message getReplyCallbackQuery(User user, String callbackQuery) {

        Session session = sessionHandler.getUserSession(user);

        Message message;
        if (callbackQuery.equals("code_process")) {
            session.enableProcessingCode();
            message = new Message("Enter code:");
        } else if (callbackQuery.startsWith("friend_delete_")) {
            message = deleteFriend(user, callbackQuery);
        } else if (callbackQuery.startsWith("friend_")) {
            message = setFriend(user, callbackQuery);
        } else if (callbackQuery.startsWith("set_language_")) {
            message = setLanguage(user, callbackQuery);
        } else {
            message = new Message("Button temperary does`t work");
        }

        userService.incrementMessageCounter();
        return message;
    }

    private Message addFriendByCode(Session session, String code) {

        session.disableProcessingCode();
        // the code contains only digits
        try {
            Long.parseLong(code);
        } catch (NumberFormatException ex) {
            return new Message("Incorrect code");
        }
        // the code is not equal to the user's own code and has the correct length
        if (!session.isCorrectLengthCode(code.length()) || code.equals(session.getInviteCode())) {
            return new Message("Incorrect code");
        }

        Optional<Session> sessionFriend = sessionHandler.findSessionFriendByCode(code);
        if (sessionFriend.isEmpty()) {
            return new Message("Friend not found by code " + code);
        }

        User user = session.getUser();
        User friend = sessionFriend.get().getUser();
        if (user.getFriends() != null) {
            for (User f : user.getFriends()) {
                if (f.equals(friend)) {
                    return new Message(String.format("%s is your friend already", friend.getName()));
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

    private Message setFriend(User user, String callBackQuery) {
        Optional<Session> session = sessionHandler.findSession(user);
        if (session.isEmpty()) {
            return getReply(user, Command.INITIAL);
        }

        int index;
        try {
            index = Integer.parseInt(callBackQuery.substring("friend_".length()));
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
                .withInitialKeyboard().build();
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
            return new Message("Something went wrong til deleting a friend");
        }

        Session sessionUser = sessionHandler.getUserSession(user);
        Optional<User> friend = sessionUser.getFriendByIndex(index);
        if (friend.isEmpty()) {
            return new Message("Friend not found!");
        }

        user.getFriends().remove(friend.get());
        friend.get().getFriends().remove(user);

        return new Message(String.format("Friend %s was deleted successfully", friend.get().getName()));
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
            return new Message("Unknown language!");
        }

        user.setLanguage(language);
        userService.save(user);

        return getReply(user, Command.INITIAL);
    }

    private void checkMatchesWithFriendAndNotify(Session session) {

        Set<Movie> movies = session.getNewMatches();
        if (movies.size() > 0) {
            this.notifyQueue.add(Pair.of(
                            session.getUser().getChatId(), messageBuilder.setLanguage(session.getUser().getLanguage())
                                    .withText("You have " + movies.size()
                                            + " matches with your friend "
                                            + session.getCurrentFriend().getName()
                                            + "!\n" + movies).build()
                    )
            ); //TODO make separate method in MessageBuilder
            Session sessionFriend = this.sessionHandler.getUserSession(session.getCurrentFriend());
            if (sessionFriend.getCurrentFriend() != null) {
                if (sessionFriend.getCurrentFriend().equals(session.getUser())) {
                    this.notifyQueue.add(Pair.of(
                                    sessionFriend.getUser().getChatId(), messageBuilder.setLanguage(sessionFriend.getUser().getLanguage())
                                            .withText("You have " + movies.size()
                                                    + " matches with your friend "
                                                    + sessionFriend.getCurrentFriend().getName()
                                                    + "!\n" + movies).build()
                            )
                    ); //TODO make separate method in MessageBuilder
                }
            }

        }
    }
}
