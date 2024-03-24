package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class MessageBuilder {
    private static final String MSG_INITIAL = "initial";
    private static final String MSG_OPTION = "select_option";
    private static final String MSG_FRIEND_INVITE = "friend.invite";
    private static final String MSG_FRIEND_INVITE_BTN = "friend.invite.btn";
    private static final String MSG_FRIEND_INVITE_INCORRECT_CODE = "friend.invite.incorrect_code";
    private static final String MSG_FRIEND_INVITE_CODE_NOT_FOUND = "friend.invite.not_found";
    private static final String MSG_FRIEND_INVITE_ALREADY_IN_LIST = "friend.invite.already_in_list_error";
    private static final String MSG_FRIEND_SELECT = "friend.select";
    private static final String MSG_FRIEND_SELECTED = "friend.selected";
    private static final String MSG_FRIEND_DELETE = "friend.delete";
    private static final String MSG_FRIEND_DELETE_SUCCESS = "friend.delete.success";
    private static final String MSG_FRIEND_NEW = "friend.new";
    private static final String MSG_FRIEND_SET_ERROR = "friend.error.set_friend";
    private static final String MSG_FRIEND_DELETE_ERROR = "friend.error.delete_friend";
    private static final String MSG_FRIEND_EMPTY_LIST = "friend.error.empty_list";
    private static final String MSG_MOVIE = "movie";
    private static final String MSG_MOVIE_FAVORITE_HEADER = "movie.favorite_header";
    private static final String MSG_MOVIE_DISLIKED_HEADER = "movie.disliked_header";
    private static final int LIST_MOVIES_BUTTONS_LENGTH = 2;
    private static final int LIST_FRIENDS_BUTTONS_LENGTH = 2;
    private static final String MSG_MOVIE_FAVORITE = "movie.favorite";
    private static final String MSG_MOVIE_DELETED_FROM_FAVORITE_LIST= "movie.favorite.deleted_successfully";
    private static final String MSG_MOVIE_DELETED_FROM_DISLIKED_LIST= "movie.disliked.deleted_successfully";
    private static final String MSG_MOVIE_NEW_MATCH = "movie.notify_new_match";
    private static final String MSG_MOVIE_MATCH_NOT_STARTED_ERROR = "movie.error.null.list";
    private static final String MSG_MOVIE_EMPTY_ERROR = "movie.error.empty.list";
    private static final String MSG_MOVIE_FAVORITE_EMPTY_ERROR = "movie.error.empty.favorite";
    private static final String MSG_MOVIE_DISLIKED_EMPTY_ERROR = "movie.error.empty.disliked";
    private static final String MSG_FRIEND_NOT_SELECTED_ERROR = "movie.error.friend_not_selected";
    private static final String MSG_MATCHES_FRIEND_EMPTY_ERROR = "movie.error.matches_friend_empty";
    private static final String MSG_NOT_FOUND_IN_FAVORITE_LIST_ERROR = "movie.error.favorite_not_found";
    private static final String MSG_NOT_FOUND_IN_DISLIKED_LIST_ERROR = "movie.error.disliked_not_found";
    private static final String MSG_SETTINGS_LANG = "settings.select_language";
    private static final String MSG_SETTINGS_LANG_ERROR = "settings.select_language.error";
    private static final String MSG_UNKNOWN_COMMAND_ERROR = "error.unknown_command";
    private static final String MSG_UNKNOWN_CALLBACK_ERROR = "error.unknown_callback";
    private static final String MSG_TRY_AGAIN_OR_CONTACT_DEVS_PS = "error.try_again_or_contact_devs";

    private static final String KBD_INITIAL = "initial";
    private static final String KBD_FRIEND = "friend";
    private static final String KBD_MOVIE = "movie";
    private static final String KBD_MOVIE_LISTS = "movie.lists";
    private static final String KBD_NEW_MATCH = "new_match";
    private static final String KBD_MATCH = "match";
    private static final String KBD_SETTINGS = "settings";

    private static final String BTN_INVITE = "friend.invite_code.";
    private static final String BTN_LANGUAGE = "settings.select_language.";
    private static final String BTN_MOVIE_REMOVE_FAVORITE = "movie.remove.favorite.";
    private static final String BTN_MOVIE_REMOVE_DISLIKED = "movie.remove.disliked.";
    private static final String BTN_SWITCH_PAGE_PREVIOUS = "switch_page.previous.";
    private static final String BTN_SWITCH_PAGE_NEXT = "switch_page.next.";


    private final MessagesKeeper messagesKeeper;
    private final KeyboardsKeeper keyboardsKeeper;
    private final ButtonsKeeper buttonsKeeper;

    private Language language;
    private String messageText;
    private List<List<String>> keyboard;
    private List<List<Pair<String, String>>> buttons;
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
        this.messageText = getMovieListHeaderText(MSG_MOVIE_FAVORITE_HEADER, MSG_MOVIE_FAVORITE_EMPTY_ERROR,
                session.getUser().getFavoriteMovies()
        );
        return this;
    }

    protected MessageBuilder withDislikedMoviesText(Session session) {
        this.messageText = getMovieListHeaderText(MSG_MOVIE_DISLIKED_HEADER, MSG_MOVIE_DISLIKED_EMPTY_ERROR,
                session.getUser().getDislikedMovies()
        );
        return this;
    }

    private String getMovieListHeaderText(String headerKey, String listEmptyKey, List<Movie> list) {
        if (list == null || list.isEmpty()) {
            return messagesKeeper.getMessage(listEmptyKey, this.language);
        }
        String headerTemplate = messagesKeeper.getMessage(headerKey, this.language);
        return String.format(headerTemplate, list.size());
    }

    protected MessageBuilder withFavoriteMoviesButtons(Session session, Integer start) {
        this.buttons = getMovieButtons(start, "movie_favorite",
                session.getUser().getFavoriteMovies());
        return this;
    }

    protected MessageBuilder withDislikedMoviesButtons(Session session, Integer start) {
        this.buttons = getMovieButtons(start, "movie_disliked",
                session.getUser().getDislikedMovies());
        return this;
    }

    private List<List<Pair<String, String>>> getMovieButtons(int start, String type, List<Movie> movieList) {
        List<List<Pair<String, String>>> buttons = new ArrayList<>();
        String movieButtonTemplate = messagesKeeper.getMessage(MSG_MOVIE_FAVORITE, this.language);

        int end = start + LIST_MOVIES_BUTTONS_LENGTH;
        end = Math.min(end, movieList.size()); //check index out of bounds

        for (int index = start; index < end; index++) {
            Movie movie = movieList.get(index);
            MovieDetails details = movie.getMovieDetails(this.language);
            String text = String.format(movieButtonTemplate, index + 1,
                    details.getName(), details.getGenre(), movie.getYearOfRelease());

            String callback = String.format("show_%s_%d", type, movie.getId());

            buttons.add(List.of(Pair.of(text, callback)));
        }
        List<Pair<String, String>> buttonsSwitchPage = getSwitchPageButtons(start, end<movieList.size(), type);
        if(!buttonsSwitchPage.isEmpty()){
            buttons.add(buttonsSwitchPage);
        }

        return (buttons.size() > 0) ? buttons : null;
    }

    private List<Pair<String, String>> getSwitchPageButtons(int startIndex, boolean hasNext, String type){
        Pair<String, String> translatedButtonNext = buttonsKeeper.getButton(BTN_SWITCH_PAGE_NEXT, this.language).get(0).get(0);
        Pair<String, String> translatedButtonPrevious = buttonsKeeper.getButton(BTN_SWITCH_PAGE_PREVIOUS, this.language).get(0).get(0);
        String callbackTemplate = translatedButtonNext.getRight();
        List<Pair<String, String>> buttonsSwitchPage = new ArrayList<>();

        if(startIndex > 0) {
            String callbackPrevious = String.format(callbackTemplate, type, startIndex - LIST_MOVIES_BUTTONS_LENGTH);
            buttonsSwitchPage.add(Pair.of(translatedButtonPrevious.getLeft(), callbackPrevious));
        }
        if(hasNext) {
            String callbackNext = String.format(callbackTemplate, type, startIndex + LIST_MOVIES_BUTTONS_LENGTH);
            buttonsSwitchPage.add(Pair.of(translatedButtonNext.getLeft(), callbackNext));
        }

        return buttonsSwitchPage;
    }

    protected MessageBuilder withMovieMatchesWithFriend(Session session) {
        List<Movie> movies = session.getMoviesMatchWithCurrentFriend();

        if (movies == null || movies.isEmpty()) {
            String template = messagesKeeper.getMessage(MSG_MATCHES_FRIEND_EMPTY_ERROR, this.language);
            this.messageText = String.format(template, session.getCurrentFriend().getName());
            return this;
        }

        this.messageText = getMovieListText(movies);
        return this;
    }

    private String getMovieListText(List<Movie> movies) {
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

    protected MessageBuilder withMovieDeletedFromFavoriteListText(Movie movie){
        String template = messagesKeeper.getMessage(MSG_MOVIE_DELETED_FROM_FAVORITE_LIST, this.language);
        this.messageText = String.format(template, movie.getMovieDetails(this.language).getName());
        return this;
    }

    protected MessageBuilder withMovieDeletedFromDislikedListText(Movie movie){
        String template = messagesKeeper.getMessage(MSG_MOVIE_DELETED_FROM_DISLIKED_LIST, this.language);
        this.messageText = String.format(template, movie.getMovieDetails(this.language).getName());
        return this;
    }

    protected MessageBuilder withMovieNotFoundInFavoriteListText() {
        this.messageText = messagesKeeper.getMessage(MSG_NOT_FOUND_IN_FAVORITE_LIST_ERROR, this.language);
        return this;
    }

    protected MessageBuilder withMovieNotFoundInDislikedListText(){
        this.messageText = messagesKeeper.getMessage(MSG_NOT_FOUND_IN_DISLIKED_LIST_ERROR, this.language);
        return this;
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
        StringBuilder builder = new StringBuilder();
        if (this.messageText != null) {
            builder.append(this.messageText);
            builder.append("\n\n");
        }
        withInitialTemplate(session);
        builder.append(this.messageText);

        this.messageText = builder.toString();

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

    public MessageBuilder withEnterInviteCodeText() {
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_INVITE_BTN, this.language);
        return this;
    }

    public MessageBuilder withIncorrectCodeText() {
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_INVITE_INCORRECT_CODE, this.language);
        return this;
    }

    public MessageBuilder withFriendNotFoundByCodeText(String enteredCode) {
        String template = messagesKeeper.getMessage(MSG_FRIEND_INVITE_CODE_NOT_FOUND, this.language);
        this.messageText = String.format(template, enteredCode);
        return this;
    }

    public MessageBuilder withFriendHasAlreadyBeenAddedText(User friend) {
        String template = messagesKeeper.getMessage(MSG_FRIEND_INVITE_ALREADY_IN_LIST, this.language);
        this.messageText = String.format(template, friend.getName());
        return this;
    }

    protected MessageBuilder withSelectFriendText(Session session) {
        if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
            this.messageText = getMessageNoFriendsYet();
            return this;
        }
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_SELECT, this.language);
        return this;
    }

    protected MessageBuilder withErrorSelectFriendText() {
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_SET_ERROR, this.language) +
                messagesKeeper.getMessage(MSG_TRY_AGAIN_OR_CONTACT_DEVS_PS, this.language);
        return this;
    }

    protected MessageBuilder withErrorDeleteFriendText() {
        this.messageText = messagesKeeper.getMessage(MSG_FRIEND_DELETE_ERROR, this.language) +
                messagesKeeper.getMessage(MSG_TRY_AGAIN_OR_CONTACT_DEVS_PS, this.language);
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

    protected MessageBuilder withFriendListButtons(Session session, Integer start) {
        List<User> friends = session.getUser().getFriends();
        List<List<Pair<String, String>>> friendsButtons = new ArrayList<>();
        String type = "friend";
        String callbackTemplate = "friend_set_%d";
        String valueTemplate = "%d. %s";
        int end = start + LIST_FRIENDS_BUTTONS_LENGTH;

        for(int index = start; index < end; index++){
            User friend = friends.get(index);
            String buttonValue = String.format(valueTemplate, index+1, friend.getName());
            String buttonCallback = String.format(callbackTemplate, friend.getId());
            friendsButtons.add(List.of(Pair.of(buttonValue, buttonCallback)));
        }

        List<Pair<String, String>> buttonsSwitchPage = getSwitchPageButtons(start, end<friends.size(), type);
        if(!buttonsSwitchPage.isEmpty()){
            friendsButtons.add(buttonsSwitchPage);
        }

        if(!friendsButtons.isEmpty()){
            this.buttons = friendsButtons;
        }
        return this;
    }

    protected MessageBuilder withFriendRemoveText(Session session) {
        if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
            this.messageText = getMessageNoFriendsYet();
        } else {
            this.messageText = messagesKeeper.getMessage(MSG_FRIEND_DELETE, this.language);
        }
        return this;
    }

    protected MessageBuilder withFriendRemovedText(User friend) {
        String template = messagesKeeper.getMessage(MSG_FRIEND_DELETE_SUCCESS, this.language);
        this.messageText = String.format(template, friend.getName());
        return this;
    }

    protected MessageBuilder withFriendRemoveButtons(Session session) {
        List<List<Pair<String, String>>> buttons = new ArrayList<>();
        if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
            this.messageText = getMessageNoFriendsYet();
        } else {
            int index = 0;
            for (User friend : session.getUser().getFriends()) {
                String removeFriendCallback = String.format("friend_delete_%d", index);
                buttons.add(List.of(Pair.of(friend.getName(), removeFriendCallback)));
                index++;
            }
            this.buttons = buttons;
        }
        return this;
    }

    private String getMessageNoFriendsYet(){
        return messagesKeeper.getMessage(MSG_FRIEND_EMPTY_LIST, this.language);
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
            this.messageText = getMessageNoFriendsYet();
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

    protected MessageBuilder withErrorUnknownCallback() {
        this.messageText = messagesKeeper.getMessage(MSG_UNKNOWN_CALLBACK_ERROR, this.language);
        return this;
    }

    protected MessageBuilder withRandomMovie(Session session) {
        Movie movie = session.getLastMovieShown();

        return withMovieMessage(movie);
    }

    protected MessageBuilder withMovieMessage(Movie movie) {
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

    public MessageBuilder withRemoveFromFavoriteButton(int movieId) {
        Pair<String, String> removeFromFavoriteBtn = buttonsKeeper.getButton(BTN_MOVIE_REMOVE_FAVORITE, this.language).get(0).get(0);
        String buttonValue = removeFromFavoriteBtn.getLeft();
        String buttonCallbackTemplate = removeFromFavoriteBtn.getRight();

        this.buttons = getRemoveFromListButton(movieId, buttonValue, buttonCallbackTemplate);
        return this;
    }

    public MessageBuilder withRemoveFromDislikedButton(int movieId) {
        Pair<String, String> removeFromDislikedBtn = buttonsKeeper.getButton(BTN_MOVIE_REMOVE_DISLIKED, this.language).get(0).get(0);
        String buttonValue = removeFromDislikedBtn.getLeft();
        String buttonCallbackTemplate = removeFromDislikedBtn.getRight();

        this.buttons = getRemoveFromListButton(movieId, buttonValue, buttonCallbackTemplate);
        return this;
    }

    private List<List<Pair<String, String>>> getRemoveFromListButton(int movieId, String message, String callbackTemplate) {
        List<Pair<String, String>> button = new ArrayList<>();

        button.add(
                Pair.of(message, String.format(callbackTemplate, movieId))
        );

        return List.of(button);
    }

    protected MessageBuilder withMovieMatchNotStarted() {
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

    protected MessageBuilder withMovieListsKeyboard() {
        this.keyboard = keyboardsKeeper.getKeyboard(KBD_MOVIE_LISTS, this.language);
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

    public MessageBuilder withSelectingLanguageErrorText() {
        this.messageText = messagesKeeper.getMessage(MSG_SETTINGS_LANG_ERROR, this.language);
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
