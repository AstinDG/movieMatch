package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class MessageBuilder {
    private static final String MSG_INITIAL = "initial";
    private static final String MSG_OPTION = "select_option";
    private static final String MSG_FRIEND_INVITE = "friend.invite";
    private static final String MSG_FRIEND_INVITE_BTN = "friend.invite.btn";
    private static final String MSG_FRIEND_SELECT = "friend.select";
    private static final String MSG_FRIEND_SELECTED = "friend.selected";
    private static final String MSG_FRIEND_DELETE = "friend.delete";
    private static final String MSG_FRIEND_NEW = "friend.new";
    private static final String MSG_FRIEND_SET_ERROR = "friend.error.set_friend";
    private static final String MSG_MOVIE = "movie";
    private static final String MSG_MOVIE_FAVORITE_HEADER = "movie.favorite_header";
    private static final String MSG_MOVIE_FAVORITE = "movie.favorite";
    private static final String MSG_MOVIE_FAVORITE_TEMPLATE = "%s\n\n%s";
    private static final String MSG_MOVIE_NEW_MATCH = "movie.notify_new_match";
    private static final String MSG_MOVIE_MATCH_NOT_STARTED_ERROR = "movie.error.null.list";
    private static final String MSG_MOVIE_EMPTY_ERROR = "movie.error.empty.list";
    private static final String MSG_MOVIE_FAVORITE_EMPTY_ERROR = "movie.error.empty.favorite";
    private static final String MSG_FRIEND_NOT_SELECTED_ERROR = "movie.error.friend_not_selected";
    private static final String MSG_MATCHES_FRIEND_EMPTY_ERROR = "movie.error.matches_friend_empty";
    private static final String MSG_SETTINGS_LANG = "settings.select_language";
    private static final String MSG_UNKNOWN_COMMAND_ERROR = "error.unknown_command";

    private static final String KBD_INITIAL = "initial";
    private static final String KBD_FRIEND = "friend";
    private static final String KBD_MOVIE = "movie";
    private static final String KBD_NEW_MATCH = "new_match";
    private static final String KBD_MATCH = "match";
    private static final String KBD_SETTINGS = "settings";

    private static final String BTN_INVITE = "friend.invite_code.";
    private static final String BTN_LANGUAGE = "settings.select_language.";


    private final MessagesKeeper messagesKeeper;
    private final KeyboardsKeeper keyboardsKeeper;
    private final ButtonsKeeper buttonsKeeper;

    private Language language;
    private String messageText;
    private List<List<String>> keyboard;
    private List<Map<String, String>> buttons;
    private File messageImage;

    @Autowired
    public MessageBuilder(MessagesKeeper messagesKeeper, KeyboardsKeeper keyboardsKeeper, ButtonsKeeper buttonsKeeper) {
        this.messagesKeeper = messagesKeeper;
        this.keyboardsKeeper = keyboardsKeeper;
        this.buttonsKeeper = buttonsKeeper;
    }

    protected MessageBuilder setLanguage(Language language) {
        this.language = language;
        return this;
    }

    protected MessageBuilder withFavoritesMoviesText(Session session) {
        if (session.getUser().getFavoriteMovies() == null || session.getUser().getFavoriteMovies().isEmpty()) {

            this.messageText = messagesKeeper.getMessage(MSG_MOVIE_FAVORITE_EMPTY_ERROR, this.language);
            return this;
        }
        Set<Movie> favoriteMovies = session.getUser().getFavoriteMovies();
        String headerTemplate = messagesKeeper.getMessage(MSG_MOVIE_FAVORITE_HEADER, this.language);
        this.messageText = String.format(headerTemplate, favoriteMovies.size());

        return this;
    }

    protected MessageBuilder withFavoriteMoviesButtons(Session session){
        Set<Movie> favoriteMovies = session.getUser().getFavoriteMovies();
        if(favoriteMovies == null || favoriteMovies.isEmpty()){
            return this;
        }

        List<Map<String, String>> buttons = new ArrayList<>();
        String movieButtonTemplate = messagesKeeper.getMessage(MSG_MOVIE_FAVORITE, this.language);
        int counter = 1;
        for(Movie movie : favoriteMovies){
            MovieDetails details = movie.getMovieDetails(this.language);
            String text = String.format(movieButtonTemplate, counter,
                    details.getName(), details.getGenre(), movie.getYearOfRelease());
            String callback = String.format("show_movie_%d", movie.getId());

            buttons.add(Map.of(text, callback));
            counter++;
        }

        this.buttons = buttons;
        return this;
    }

    protected MessageBuilder withMovieMatchesWithFriend(Session session) {
        Set<Movie> movies = session.getMoviesMatchWithCurrentFriend();

        if(movies == null || movies.isEmpty()){
            String template = messagesKeeper.getMessage(MSG_MATCHES_FRIEND_EMPTY_ERROR, this.language);
            this.messageText = String.format(template, session.getCurrentFriend().getName());
            return this;
        }

        this.messageText = getMovieListText(movies);
        return this;
    }

    private String getMovieListText(Set<Movie> movies){
        StringBuilder moviesSB = new StringBuilder();
        int number = 1;

        //TODO rename MSG_MOVIE_FAVORITE (used in withFavoritesMoviesText and withMovieMatchesWithFriend methods)
        String template = messagesKeeper.getMessage(MSG_MOVIE_FAVORITE, this.language);

        for (Movie movie : movies) {
            MovieDetails movieDetails = movie.getMovieDetails(this.language);

            moviesSB.append(String.format(template,
                    number, movieDetails.getName(), movieDetails.getGenre(), movieDetails.getYearOfRelease()));
            number++;
        }

        return moviesSB.toString();
    }

    protected MessageBuilder withSelectOptionText() {
        this.messageText = messagesKeeper.getMessage(MSG_OPTION, this.language);
        return this;
    }

    protected MessageBuilder withNotifyNewFriendText(Session friendsSession) {
        String template = messagesKeeper.getMessage(MSG_FRIEND_NEW, this.language);
        this.messageText = String.format(template, friendsSession.getUser().getName());
        return this;
    }

    protected MessageBuilder withInitialTemplate(Session session) {

        User user = session.getUser();
        User friend = session.getCurrentFriend();
        String friendName = (friend != null) ? friend.getName() : "---";
        int countFriends = (user.getFriends() != null) ? user.getFriends().size() : 0;

        String template = messagesKeeper.getMessage(MSG_INITIAL, this.language);
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

        this.keyboard = keyboardsKeeper.getKeyboard(KBD_INITIAL, this.language);

        return this;
    }

    protected MessageBuilder withInviteFriendTemplate(Session session) {
        if (session.getInviteCode() != null) {
            String template = messagesKeeper.getMessage(MSG_FRIEND_INVITE, this.language);
            this.messageText = String.format(template, session.getInviteCode(), session.getInviteCodeMaxAge());
        }
        return this;
    }

    protected MessageBuilder withEnterInviteCodeButton() {

        this.buttons = buttonsKeeper.getButton(BTN_INVITE, this.language);

        return this;
    }

    public MessageBuilder withEnterInviteCodeText(){
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_INVITE_BTN, this.language);
        return this;
    }

    protected MessageBuilder withSelectFriendText(Session session) {
        if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
            //TODO make separate method
            this.messageText = "You have no friend yet...";
            return this;
        }
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_SELECT, this.language);
        return this;
    }

    protected MessageBuilder withErrorSelectFriendText() {
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_SET_ERROR, this.language);
        return this;
    }

    protected MessageBuilder withFriendSelectedText(Session session) {
        String template = messagesKeeper.getMessage(MSG_FRIEND_SELECTED, this.language);
        this.messageText = String.format(template, session.getCurrentFriend().getName());
        return this;
    }

    protected MessageBuilder withFriendNotSelectedError() {
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_NOT_SELECTED_ERROR, this.language);
        return this;
    }

    protected MessageBuilder withFriendListButtons(Session session) {
        if (session.getUser().getFriends() != null && !session.getUser().getFriends().isEmpty()) {
            List<User> friendList = session.getUser().getFriends();
            List<Map<String, String>> friends = new ArrayList<>();

            for (int i = 0; i < session.getUser().getFriends().size(); i++) {
                User friend = friendList.get(i);

                //TODO make separate method in ButtonsKeeper
                friends.add(Map.of(friend.getName(), String.format("friend_%d", i)));
            }
            this.buttons = friends;

        }
        return this;
    }

    protected MessageBuilder withFriendRemoveText() {
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_DELETE, this.language);
        return this;
    }

    protected MessageBuilder withFriendRemoveButtons(Session session) {
        List<Map<String, String>> buttons = new ArrayList<>();
        if (session.getUser().getFriends() == null) {
            //TODO make separate method in MessageKeeper
            this.messageText = "You have no friend yet...";
        } else {
            int index = 0;
            for (User friend : session.getUser().getFriends()) {
                //TODO make separate method in ButtonsKeeper
                buttons.add(Map.of(friend.getName(), String.format("friend_delete_%d", index)));
                index++;
            }
            this.buttons = buttons;
        }
        return this;
    }

    protected MessageBuilder withNewMatchMovieMessage(Session session) {
        Movie movie = session.getNewMatchMovie();
        String movieName = movie.getMovieDetails(this.language).getName();
        User friend = session.getCurrentFriend();

        String template = messagesKeeper.getMessage(MSG_MOVIE_NEW_MATCH, this.language);

        this.messageText = String.format(template, friend.getName(), movieName);
        return this;
    }

    protected MessageBuilder withNewMatchKeyBoard() {
        this.keyboard = keyboardsKeeper.getKeyboard(KBD_NEW_MATCH, this.language);
        return this;
    }

    protected MessageBuilder withFriendListText(Session session) {
        if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
            //TODO make separate method in MessageKeeper
            this.messageText = "You have no friends yet";
            return this;
        }

        StringBuilder friendList = new StringBuilder();
        int number = 1;
        for (User friend : session.getUser().getFriends()) {
            //TODO make separate method in MessageKeeper
            friendList.append(String.format("%d. %s\n", number, friend.getName()));
            number++;
        }
        this.messageText = friendList.toString();
        return this;
    }

    protected MessageBuilder withErrorUnknownCommand() {
        this.messageText = messagesKeeper.getMessage(MSG_UNKNOWN_COMMAND_ERROR, this.language);
        return this;
    }

    protected MessageBuilder withRandomMovie(Session session) {
        Movie movie = session.getLastMovieShown();
        String template = messagesKeeper.getMessage(MSG_MOVIE, this.language);
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

    protected MessageBuilder withMovieMatchNotStarted(){
        this.messageText = messagesKeeper.getMessage(MSG_MOVIE_MATCH_NOT_STARTED_ERROR, this.language);
        return this;
    }

    protected MessageBuilder withNoMoviesText() {
        this.messageText = messagesKeeper.getMessage(MSG_MOVIE_EMPTY_ERROR, this.language);
        return this;
    }

    protected MessageBuilder withMovieMatchKeyboard() {
        this.keyboard = keyboardsKeeper.getKeyboard(KBD_MATCH, this.language);
        return this;
    }

    protected MessageBuilder withFriendMenuKeyboard() {
        this.keyboard = keyboardsKeeper.getKeyboard(KBD_FRIEND, this.language);
        return this;
    }

    protected MessageBuilder withMovieMenuKeyboard() {
        this.keyboard = keyboardsKeeper.getKeyboard(KBD_MOVIE, this.language);
        return this;
    }

    protected MessageBuilder withSettingsKeyboard() {
        this.keyboard = keyboardsKeeper.getKeyboard(KBD_SETTINGS, this.language);
        return this;
    }

    public MessageBuilder withSelectLanguageText() {
        this.messageText = messagesKeeper.getMessage(MSG_SETTINGS_LANG, this.language);
        return this;
    }

    public MessageBuilder withSelectLanguageButtons() {
        this.buttons = buttonsKeeper.getButton(BTN_LANGUAGE, this.language);
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

        this.language = null;

        return message;
    }

}
