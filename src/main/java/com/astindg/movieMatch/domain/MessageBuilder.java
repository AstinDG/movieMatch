package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.*;
import com.astindg.movieMatch.util.exceptions.MessageBuilderSetupException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class MessageBuilder {

    private static final String MSG_MOVIE = "movie";

    private final MessagesKeeper messagesKeeper;
    private final KeyboardsKeeper keyboardsKeeper;
    private final ButtonsKeeper buttonsKeeper;

    private final MessageText textObj;
    private final Buttons buttonsObj;
    private final Keyboards keyboardsObj;

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
        this.textObj = new MessageText();
        this.buttonsObj = new Buttons();
        this.keyboardsObj = new Keyboards();
    }

    public MessageBuilder setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public MessageBuilder getRandomMovie(Session session) throws MessageBuilderSetupException {
        if (this.language == null) {
            throw new MessageBuilderSetupException("An error occurred while calling getRandomMovie(Session session): Language is required");
        }

        Movie movie = session.getLastRandomMovie();

        return getMovieMessage(movie);
    }

    public MessageBuilder getMovieMessage(Movie movie) throws MessageBuilderSetupException {
        if (this.language == null) {
            throw new MessageBuilderSetupException("An error occurred while calling getMovieMessage(Movie movie): Language is required");
        }

        String template = messagesKeeper.getMessage(MSG_MOVIE, this.language);
        MovieDetails movieDetails = movie.getMovieDetails(this.language);

        this.messageText = String.format(template,
                movieDetails.getName(),
                movieDetails.getGenre(),
                movieDetails.getYearOfRelease(),
                movieDetails.getDescription()
        );

        this.messageImage = movieDetails.getImage();

        return this;
    }

    public MessageText getText() throws MessageBuilderSetupException {
        if (this.language == null) {
            throw new MessageBuilderSetupException("An error occurred while calling getText(): Language is required");
        }

        return this.textObj;
    }

    public Keyboards getKeyboards() throws MessageBuilderSetupException {
        if (this.buttons != null) {
            throw new MessageBuilderSetupException("An error occurred while calling getKeyboards(): Buttons are already installed");
        }
        if (this.language == null) {
            throw new MessageBuilderSetupException("An error occurred while calling getKeyboards(): Language is required");
        }

        return this.keyboardsObj;
    }

    public Buttons getButtons() throws MessageBuilderSetupException {
        if (this.keyboard != null) {
            throw new MessageBuilderSetupException("An error occurred while calling getButtons(): Keyboard are already installed");
        }
        if (this.language == null) {
            throw new MessageBuilderSetupException("An error occurred while calling getButtons(): Language is required");
        }

        return this.buttonsObj;
    }

    public Message build() {
        Message message = new Message(this.messageText, this.buttons, this.keyboard, this.messageImage);

        resetFields();

        return message;
    }

    private void resetFields() {
        this.messageText = null;
        this.keyboard = null;
        this.buttons = null;
        this.messageImage = null;
        this.language = null;
    }

    public class MessageText {

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
        private static final String MSG_MOVIE_FAVORITE_HEADER = "movie.favorite_header";
        private static final String MSG_MOVIE_DISLIKED_HEADER = "movie.disliked_header";
        private static final String MSG_MOVIE_FAVORITE = "movie.favorite";
        private static final String MSG_MOVIE_DELETED_FROM_FAVORITE_LIST = "movie.favorite.deleted_successfully";
        private static final String MSG_MOVIE_DELETED_FROM_DISLIKED_LIST = "movie.disliked.deleted_successfully";
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

        private MessageText() {

        }

        public MessageBuilder initial(Session session) {
            User user = session.getUser();
            User friend = session.getCurrentFriend();
            String friendName = (friend != null) ? friend.getName() : "---";
            int countFriends = (user.getFriends() != null) ? user.getFriends().size() : 0;

            String template = messagesKeeper.getMessage(MSG_INITIAL, MessageBuilder.this.language);
            MessageBuilder.this.messageText = String.format(template, user.getName(), friendName, countFriends);

            return MessageBuilder.this;
        }

        public MessageBuilder selectOption() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_OPTION, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder unknownCommand() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_UNKNOWN_COMMAND_ERROR, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder unknownCallback() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_UNKNOWN_CALLBACK_ERROR, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder selectLanguage() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_SETTINGS_LANG, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder selectLanguageError() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_SETTINGS_LANG_ERROR, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder inviteCode() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_FRIEND_INVITE_BTN, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder inviteCodeError() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_FRIEND_INVITE_INCORRECT_CODE, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        /*######### Topic: Movie ##########*/
        public MessageBuilder favoritesMovies(Session session) {
            MessageBuilder.this.messageText = getMovieListText(MSG_MOVIE_FAVORITE_HEADER, MSG_MOVIE_FAVORITE_EMPTY_ERROR,
                    session.getUser().getFavoriteMovies()
            );
            return MessageBuilder.this;
        }

        public MessageBuilder dislikedMovies(Session session) {
            MessageBuilder.this.messageText = getMovieListText(MSG_MOVIE_DISLIKED_HEADER, MSG_MOVIE_DISLIKED_EMPTY_ERROR,
                    session.getUser().getDislikedMovies()
            );
            return MessageBuilder.this;
        }

        private String getMovieListText(String headerKey, String listEmptyKey, List<Movie> list) {
            if (list == null || list.isEmpty()) {
                return messagesKeeper.getMessage(listEmptyKey, MessageBuilder.this.language);
            }
            String headerTemplate = messagesKeeper.getMessage(headerKey, MessageBuilder.this.language);
            return String.format(headerTemplate, list.size());
        }

        public MessageBuilder movieMatchNotStarted() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_MOVIE_MATCH_NOT_STARTED_ERROR, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder moviesListEmpty() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_MOVIE_EMPTY_ERROR, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder movieMatchesWithFriend(Session session) {
            List<Movie> movies = session.getMoviesMatchWithCurrentFriend();

            if (movies == null || movies.isEmpty()) {
                String template = messagesKeeper.getMessage(MSG_MATCHES_FRIEND_EMPTY_ERROR, MessageBuilder.this.language);
                MessageBuilder.this.messageText = String.format(template, session.getCurrentFriend().getName());
                return MessageBuilder.this;
            }

            StringBuilder moviesSB = new StringBuilder();
            String template = messagesKeeper.getMessage(MSG_MOVIE_FAVORITE, MessageBuilder.this.language);
            int number = 1;
            for (Movie movie : movies) {
                MovieDetails movieDetails = movie.getMovieDetails(MessageBuilder.this.language);

                moviesSB.append(String.format(template,
                        number, movieDetails.getName(), movieDetails.getGenre(), movieDetails.getYearOfRelease()));
                number++;
            }

            MessageBuilder.this.messageText = moviesSB.toString();
            return MessageBuilder.this;
        }

        public MessageBuilder movieDeletedFromFavorite(Movie movie) {
            String template = messagesKeeper.getMessage(MSG_MOVIE_DELETED_FROM_FAVORITE_LIST, MessageBuilder.this.language);
            MessageBuilder.this.messageText = String.format(template, movie.getMovieDetails(MessageBuilder.this.language).getName());
            return MessageBuilder.this;
        }

        public MessageBuilder movieDeletedFromDisliked(Movie movie) {
            String template = messagesKeeper.getMessage(MSG_MOVIE_DELETED_FROM_DISLIKED_LIST, MessageBuilder.this.language);
            MessageBuilder.this.messageText = String.format(template, movie.getMovieDetails(MessageBuilder.this.language).getName());
            return MessageBuilder.this;
        }

        public MessageBuilder movieNotFoundInFavorite() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_NOT_FOUND_IN_FAVORITE_LIST_ERROR, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder movieNotFoundInDisliked() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_NOT_FOUND_IN_DISLIKED_LIST_ERROR, MessageBuilder.this.language);
            return MessageBuilder.this;
        }


        public MessageBuilder newMatch(Session session) {
            Movie movie = session.getNewMatchMovie();
            String movieName = movie.getMovieDetails(MessageBuilder.this.language).getName();
            User friend = session.getCurrentFriend();

            String template = messagesKeeper.getMessage(MSG_MOVIE_NEW_MATCH, MessageBuilder.this.language);

            MessageBuilder.this.messageText = String.format(template, friend.getName(), movieName);
            return MessageBuilder.this;
        }

        /*## End of topic: Movie ##########*/

        /*######### Topic: Friend #########*/

        public MessageBuilder selectFriend(Session session) {
            if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
                MessageBuilder.this.messageText = getMessageNoFriendsYet();
                return MessageBuilder.this;
            }
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_FRIEND_SELECT, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder friendSelected(Session session) {
            String template = messagesKeeper.getMessage(MSG_FRIEND_SELECTED, MessageBuilder.this.language);
            MessageBuilder.this.messageText = String.format(template, session.getCurrentFriend().getName());
            return MessageBuilder.this;
        }

        public MessageBuilder notifyNewFriend(Session friendsSession) {
            String template = messagesKeeper.getMessage(MSG_FRIEND_NEW, MessageBuilder.this.language);
            MessageBuilder.this.messageText = String.format(template, friendsSession.getUser().getName());
            return MessageBuilder.this;
        }

        public MessageBuilder inviteFriend(Session session) {
            if (session.getInviteCode() != null) {
                String template = messagesKeeper.getMessage(MSG_FRIEND_INVITE, MessageBuilder.this.language);
                MessageBuilder.this.messageText = String.format(template, session.getInviteCode(), session.getInviteCodeMaxAge());
            }
            return MessageBuilder.this;
        }

        public MessageBuilder friendNotFound(String enteredCode) {
            String template = messagesKeeper.getMessage(MSG_FRIEND_INVITE_CODE_NOT_FOUND, MessageBuilder.this.language);
            MessageBuilder.this.messageText = String.format(template, enteredCode);
            return MessageBuilder.this;
        }

        public MessageBuilder friendAlready(User friend) {
            String template = messagesKeeper.getMessage(MSG_FRIEND_INVITE_ALREADY_IN_LIST, MessageBuilder.this.language);
            MessageBuilder.this.messageText = String.format(template, friend.getName());
            return MessageBuilder.this;
        }

        public MessageBuilder friendSelectError() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_FRIEND_SET_ERROR, MessageBuilder.this.language) +
                    messagesKeeper.getMessage(MSG_TRY_AGAIN_OR_CONTACT_DEVS_PS, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder friendDeleteError() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_FRIEND_DELETE_ERROR, MessageBuilder.this.language) +
                    messagesKeeper.getMessage(MSG_TRY_AGAIN_OR_CONTACT_DEVS_PS, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder friendNotSelectedError() {
            MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_FRIEND_NOT_SELECTED_ERROR, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder friendRemove(Session session) {
            if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
                MessageBuilder.this.messageText = getMessageNoFriendsYet();
            } else {
                MessageBuilder.this.messageText = messagesKeeper.getMessage(MSG_FRIEND_DELETE, MessageBuilder.this.language);
            }
            return MessageBuilder.this;
        }

        public MessageBuilder friendRemoved(User removedFriend) {
            String template = messagesKeeper.getMessage(MSG_FRIEND_DELETE_SUCCESS, MessageBuilder.this.language);
            MessageBuilder.this.messageText = String.format(template, removedFriend.getName());
            return MessageBuilder.this;
        }

        public MessageBuilder friendList(Session session) {
            if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
                MessageBuilder.this.messageText = getMessageNoFriendsYet();
                return MessageBuilder.this;
            }

            StringBuilder friendList = new StringBuilder();
            int number = 1;
            for (User friend : session.getUser().getFriends()) {
                //TODO make separate method in MessageKeeper
                friendList.append(String.format("%d. %s\n", number, friend.getName()));
                number++;
            }
            MessageBuilder.this.messageText = friendList.toString();
            return MessageBuilder.this;
        }

        private String getMessageNoFriendsYet() {
            return messagesKeeper.getMessage(MSG_FRIEND_EMPTY_LIST, MessageBuilder.this.language);
        }

        /*## End of topic: Friend #########*/
    }

    public class Buttons {
        private static final String BTN_INVITE = "friend.invite_code.";
        private static final String BTN_LANGUAGE = "settings.select_language.";
        private static final String BTN_MOVIE_REMOVE_FAVORITE = "movie.remove.favorite.";
        private static final String BTN_MOVIE_REMOVE_DISLIKED = "movie.remove.disliked.";
        private static final String BTN_SWITCH_PAGE_PREVIOUS = "switch_page.previous.";
        private static final String BTN_SWITCH_PAGE_NEXT = "switch_page.next.";

        private static final String MSG_MOVIE_FAVORITE = "movie.favorite";

        private static final int LIST_MOVIES_BUTTONS_LENGTH = 5;
        private static final int LIST_FRIENDS_BUTTONS_LENGTH = 5;

        private Buttons() {

        }

        public MessageBuilder inviteCode() {
            MessageBuilder.this.buttons = buttonsKeeper.getButton(BTN_INVITE, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder selectLanguage() {
            MessageBuilder.this.buttons = buttonsKeeper.getButton(BTN_LANGUAGE, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder friendList(Session session, Integer start) {
            List<User> friends = session.getUser().getFriends();
            List<List<Pair<String, String>>> friendsButtons = new ArrayList<>();
            String type = "friend";
            String callbackTemplate = "friend_set_%d";
            String valueTemplate = "%d. %s";
            int end = start + LIST_FRIENDS_BUTTONS_LENGTH;
            end = Math.min(end, friends.size()); //check index out of bounds

            for (int index = start; index < end; index++) {
                User friend = friends.get(index);
                String buttonValue = String.format(valueTemplate, index + 1, friend.getName());
                String buttonCallback = String.format(callbackTemplate, friend.getId());
                friendsButtons.add(List.of(Pair.of(buttonValue, buttonCallback)));
            }

            List<Pair<String, String>> buttonsSwitchPage = getSwitchPageButtons(start, end < friends.size(), type);
            if (!buttonsSwitchPage.isEmpty()) {
                friendsButtons.add(buttonsSwitchPage);
            }

            if (!friendsButtons.isEmpty()) {
                MessageBuilder.this.buttons = friendsButtons;
            }
            return MessageBuilder.this;
        }

        public MessageBuilder friendRemove(Session session) {
            List<List<Pair<String, String>>> buttons = new ArrayList<>();
            if (session.getUser().getFriends() == null || session.getUser().getFriends().isEmpty()) {
                MessageBuilder.this.messageText = getText().getMessageNoFriendsYet();
            } else {
                for (User friend : session.getUser().getFriends()) {
                    String removeFriendCallback = String.format("friend_delete_%d", friend.getId());
                    buttons.add(List.of(Pair.of(friend.getName(), removeFriendCallback)));
                }
                MessageBuilder.this.buttons = buttons;
            }
            return MessageBuilder.this;
        }

        public MessageBuilder removeFromFavorite(int movieId) {
            Pair<String, String> removeFromFavoriteBtn = buttonsKeeper.getButton(BTN_MOVIE_REMOVE_FAVORITE, MessageBuilder.this.language).get(0).get(0);
            String buttonValue = removeFromFavoriteBtn.getLeft();
            String buttonCallbackTemplate = removeFromFavoriteBtn.getRight();

            MessageBuilder.this.buttons = getRemoveFromListButton(movieId, buttonValue, buttonCallbackTemplate);
            return MessageBuilder.this;
        }

        public MessageBuilder removeFromDisliked(int movieId) {
            Pair<String, String> removeFromDislikedBtn = buttonsKeeper.getButton(BTN_MOVIE_REMOVE_DISLIKED, MessageBuilder.this.language).get(0).get(0);
            String buttonValue = removeFromDislikedBtn.getLeft();
            String buttonCallbackTemplate = removeFromDislikedBtn.getRight();

            MessageBuilder.this.buttons = getRemoveFromListButton(movieId, buttonValue, buttonCallbackTemplate);
            return MessageBuilder.this;
        }

        private List<List<Pair<String, String>>> getRemoveFromListButton(int movieId, String message, String callbackTemplate) {
            List<Pair<String, String>> button = new ArrayList<>();

            button.add(
                    Pair.of(message, String.format(callbackTemplate, movieId))
            );

            return List.of(button);
        }

        public MessageBuilder favoriteMovies(Session session, Integer start) {
            MessageBuilder.this.buttons = getMovieButtons(start, "movie_favorite",
                    session.getUser().getFavoriteMovies());
            return MessageBuilder.this;
        }

        public MessageBuilder dislikedMovies(Session session, Integer start) {
            MessageBuilder.this.buttons = getMovieButtons(start, "movie_disliked",
                    session.getUser().getDislikedMovies());
            return MessageBuilder.this;
        }

        private List<List<Pair<String, String>>> getMovieButtons(int start, String type, List<Movie> movieList) {
            List<List<Pair<String, String>>> buttons = new ArrayList<>();
            String movieButtonTemplate = messagesKeeper.getMessage(MSG_MOVIE_FAVORITE, MessageBuilder.this.language);

            int end = start + LIST_MOVIES_BUTTONS_LENGTH;
            end = Math.min(end, movieList.size()); //check index out of bounds

            for (int index = start; index < end; index++) {
                Movie movie = movieList.get(index);
                MovieDetails details = movie.getMovieDetails(MessageBuilder.this.language);
                String text = String.format(movieButtonTemplate, index + 1,
                        details.getName(), details.getGenre(), movie.getYearOfRelease());

                String callback = String.format("show_%s_%d", type, movie.getId());

                buttons.add(List.of(Pair.of(text, callback)));
            }
            List<Pair<String, String>> buttonsSwitchPage = getSwitchPageButtons(start, end < movieList.size(), type);
            if (!buttonsSwitchPage.isEmpty()) {
                buttons.add(buttonsSwitchPage);
            }

            return (buttons.size() > 0) ? buttons : null;
        }

        private List<Pair<String, String>> getSwitchPageButtons(int startIndex, boolean hasNext, String type) {
            Pair<String, String> translatedButtonNext = buttonsKeeper.getButton(BTN_SWITCH_PAGE_NEXT, MessageBuilder.this.language).get(0).get(0);
            Pair<String, String> translatedButtonPrevious = buttonsKeeper.getButton(BTN_SWITCH_PAGE_PREVIOUS, MessageBuilder.this.language).get(0).get(0);
            String callbackTemplate = translatedButtonNext.getRight();
            List<Pair<String, String>> buttonsSwitchPage = new ArrayList<>();

            if (startIndex > 0) {
                String callbackPrevious = String.format(callbackTemplate, type, startIndex - LIST_MOVIES_BUTTONS_LENGTH);
                buttonsSwitchPage.add(Pair.of(translatedButtonPrevious.getLeft(), callbackPrevious));
            }
            if (hasNext) {
                String callbackNext = String.format(callbackTemplate, type, startIndex + LIST_MOVIES_BUTTONS_LENGTH);
                buttonsSwitchPage.add(Pair.of(translatedButtonNext.getLeft(), callbackNext));
            }

            return buttonsSwitchPage;
        }
    }

    public class Keyboards {
        private static final String KBD_INITIAL = "initial";
        private static final String KBD_FRIEND = "friend";
        private static final String KBD_MOVIE = "movie";
        private static final String KBD_MOVIE_LISTS = "movie.lists";
        private static final String KBD_NEW_MATCH = "new_match";
        private static final String KBD_MATCH = "match";
        private static final String KBD_SETTINGS = "settings";

        private Keyboards() {

        }

        public MessageBuilder initial() {
            MessageBuilder.this.keyboard = keyboardsKeeper.getKeyboard(KBD_INITIAL, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder friends() {
            MessageBuilder.this.keyboard = keyboardsKeeper.getKeyboard(KBD_FRIEND, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder movie() {
            MessageBuilder.this.keyboard = keyboardsKeeper.getKeyboard(KBD_MOVIE, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder movieLists() {
            MessageBuilder.this.keyboard = keyboardsKeeper.getKeyboard(KBD_MOVIE_LISTS, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder newMatch() {
            MessageBuilder.this.keyboard = keyboardsKeeper.getKeyboard(KBD_NEW_MATCH, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder movieMatch() {
            MessageBuilder.this.keyboard = keyboardsKeeper.getKeyboard(KBD_MATCH, MessageBuilder.this.language);
            return MessageBuilder.this;
        }

        public MessageBuilder settings() {
            MessageBuilder.this.keyboard = keyboardsKeeper.getKeyboard(KBD_SETTINGS, MessageBuilder.this.language);
            return MessageBuilder.this;
        }
    }
}