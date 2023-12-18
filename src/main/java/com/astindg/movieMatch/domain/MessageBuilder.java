package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MessageBuilder {

    private final MessageKeeper messageKeeper;

    private Language language;
    private String messageText;
    private List<List<String>> keyboard;
    private List<Map<String, String>> buttons;
    private File messageImage;

    public MessageBuilder(MessageKeeper messageKeeper) {
        this.messageKeeper = messageKeeper;
    }

    protected MessageBuilder setLanguage(Language language) {
        this.language = language;
        return this;
    }

    protected MessageBuilder withText(String text) {

        this.messageText = text;

        return this;
    }

    protected MessageBuilder withFavoritesMoviesText(Session session) {
        if (session.getUser().getFavoriteMovies() == null || session.getUser().getFavoriteMovies().isEmpty()) {

            this.messageText = messageKeeper.getMessage("movie.error.empty.favorite", this.language);
            return this;
        }

        StringBuilder movies = new StringBuilder();
        int number = 1;

        String template = messageKeeper.getMessage("movie.favorite", this.language);

        for (Movie movie : session.getUser().getFavoriteMovies()) {
            MovieDetails movieDetails = movie.getMovieDetails(this.language);

            movies.append(String.format(template,
                    number,
                    movieDetails.getName(), movieDetails.getGenre(), movieDetails.getYearOfRelease()));
            number++;
        }


        this.messageText = movies.toString();
        return this;
    }

    protected MessageBuilder withSelectOptionText() {
        this.messageText = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.SELECT_OPTION_KEY, this.language);
        return this;
    }

    protected MessageBuilder withNotifyNewFriendText(Session friendsSession) {
        String template = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.NOTIFY_NEW_FRIEND_KEY, this.language);
        this.messageText = String.format(template, friendsSession.getUser().getName());
        return this;
    }

    protected MessageBuilder withInitialTemplate(Session session) {

        User user = session.getUser();
        User friend = session.getCurrentFriend();
        String friendName = (friend != null) ? friend.getName() : "---";
        int countFriends = (user.getFriends() != null) ? user.getFriends().size() : 0;

        String template = messageKeeper.getMessage("initial", this.language);
        this.messageText = String.format(template, user.getName(), friendName, countFriends);

        return this;
    }

    protected MessageBuilder appendWithInitialMessage(Session session) {

        if (this.messageText != null) {
            StringBuilder builder = new StringBuilder(this.messageText);
            builder.append("\n\n");
            withInitialTemplate(session);
            builder.append(this.messageText);

            this.messageText = builder.toString();
        }
        return this;
    }

    protected MessageBuilder withInitialKeyboard() {

        this.keyboard = MessageTemplateKeeper.getKeyboardByKey(MessageTemplateKeeper.INITIAL_KEYBOARD_KEY, this.language);

        return this;
    }

    protected MessageBuilder withInviteFriendTemplate(Session session) {
        if (session.getInviteCode() != null) {
            String template = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.INVITE_FRIEND_KEY, this.language);
            this.messageText = String.format(template, session.getInviteCode(), session.getInviteCodeMaxAge());
        }
        return this;
    }

    protected MessageBuilder withEnterInviteCodeButton() {

        this.buttons = MessageTemplateKeeper.getButtonsByKey(MessageTemplateKeeper.INVITE_FRIEND_BUTTON_KEY, this.language);

        return this;
    }

    protected MessageBuilder withSelectFriendText() {
        this.messageText = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.SELECT_FRIEND_KEY, this.language);
        return this;
    }

    protected MessageBuilder withErrorSelectFriendText() {
        this.messageText = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.ERROR_MESSAGE_SETTING_FRIEND_KEY, this.language);
        return this;
    }

    protected MessageBuilder withFriendSelectedText(Session session) {
        String template = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.FRIEND_SELECTED_KEY, this.language);
        this.messageText = String.format(template, session.getCurrentFriend().getName());
        return this;
    }

    protected MessageBuilder withFriendNotSelectedError() {
        this.messageText = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.ERROR_MESSAGE_FRIEND_NOT_SELECTED_KEY, this.language);
        return this;
    }

    protected MessageBuilder withFriendListButtons(Session session) {
        User user = session.getUser();
        if (user.getFriends() != null && !user.getFriends().isEmpty()) {
            List<Map<String, String>> friends = new ArrayList<>();

            for (int i = 0; i < user.getFriends().size(); i++) {
                User friend = user.getFriends().get(i);

                friends.add(Map.of(friend.getName(), String.format("friend_%d", i)));
            }
            this.buttons = friends;

        } else {
            this.messageText = "You have no friend yet...";
        }
        return this;
    }

    protected MessageBuilder withFriendRemoveText() {
        this.messageText = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.SELECT_FRIEND_TO_DELETE_KEY, this.language);
        return this;
    }

    protected MessageBuilder withFriendRemoveButtons(Session session) {
        List<Map<String, String>> buttons = new ArrayList<>();
        if (session.getUser().getFriends() == null) {
            this.messageText = "You have no friend yet...";
        } else {
            int index = 0;
            for (User friend : session.getUser().getFriends()) {
                buttons.add(Map.of(friend.getName(), String.format("friend_delete_%d", index)));
                index++;
            }
            this.buttons = buttons;
        }
        return this;
    }

    protected MessageBuilder withFriendListText(Session session) {
        if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
            this.messageText = "You have no friends yet";
            return this;
        }

        StringBuilder friendList = new StringBuilder();
        int number = 1;
        for (User friend : session.getUser().getFriends()) {
            friendList.append(String.format("%d. %s\n", number, friend.getName()));
            number++;
        }
        this.messageText = friendList.toString();
        return this;
    }

    protected MessageBuilder withErrorUnknownCommand() {
        this.messageText = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.ERROR_MESSAGE_UNKNOWN_COMMAND_KEY, this.language);
        return this;
    }

   protected MessageBuilder withRandomMovie(Session session) {
        Movie movie = session.getLastMovieShown();
        String template = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.MOVIE_MESSAGE_KEY, this.language);
        MovieDetails movieDetails = movie.getMovieDetails(language);

       this.messageText = String.format(template,
               movieDetails.getName(),
               movieDetails.getGenre(),
               movieDetails.getYearOfRelease(),
               movieDetails.getDescription()
       );

        this.messageImage = movieDetails.getImage();

        return this;
    }

    protected MessageBuilder withNoMoviesText() {
        this.messageText = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.NO_NEW_MOVIES_KEY, this.language);
        return this;
    }

    protected MessageBuilder withMovieMatchKeyboard() {
        this.keyboard = MessageTemplateKeeper.getKeyboardByKey(MessageTemplateKeeper.MOVIE_MATCH_KEYBOARD_KEY, this.language);
        return this;
    }

    protected MessageBuilder withFriendMenuKeyboard() {
        this.keyboard = MessageTemplateKeeper.getKeyboardByKey(MessageTemplateKeeper.FRIEND_KEYBOARD_KEY, this.language);
        return this;
    }

    protected MessageBuilder withMovieMenuKeyBoard() {
        this.keyboard = MessageTemplateKeeper.getKeyboardByKey(MessageTemplateKeeper.MOVIE_KEYBOARD_KEY, this.language);
        return this;
    }

    protected MessageBuilder withSettingsKeyboard() {
        this.keyboard = MessageTemplateKeeper.getKeyboardByKey(MessageTemplateKeeper.SETTINGS_KEYBOARD_KEY, this.language);
        return this;
    }

    public MessageBuilder withSelectLanguageText() {
        this.messageText = MessageTemplateKeeper.getMessageOrTemplateByKey(MessageTemplateKeeper.SELECT_LANGUAGE_KEY, this.language);
        return this;
    }

    public MessageBuilder withSelectLanguageButtons() {
        this.buttons = MessageTemplateKeeper.getButtonsByKey(MessageTemplateKeeper.SELECT_LANGUAGE_BUTTONS_KEY, this.language);
        return this;
    }

    protected Message build() {

        Message message = new Message();

        message.setText(this.messageText);
        this.messageText = null;

        message.setKeyboard(this.keyboard);
        this.keyboard = null;

        message.setButtons(this.buttons);
        this.buttons = null;

        message.setImage(this.messageImage);
        this.messageImage = null;

        return message;
    }

}
