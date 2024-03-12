package com.astindg.movieMatch.telegram;

import com.astindg.movieMatch.domain.Command;
import com.astindg.movieMatch.model.Language;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public record CommandTranslator(Environment env) {
    private static final Map<String, Command> commandMapTemplate = new HashMap<>();

    private static final String INITIAL_COMMAND = "/start";
    private static final String ADD_FRIEND = "user.keyboard.friend.add.";
    private static final String RETURN = "user.keyboard.button.return_main.";
    private static final String SELECT_FRIEND = "user.keyboard.friend.set.";
    private static final String START_MATCH = "user.keyboard.movie.start_match.";
    private static final String CONTINUE_MATCH = "user.keyboard.new_match.continue.";
    private static final String LIKE = "user.keyboard.match.like.";
    private static final String DISLIKE = "user.keyboard.match.dislike.";
    private static final String FRIENDS_MENU = "user.keyboard.initial.friends.";
    private static final String MOVIE_MENU = "user.keyboard.initial.movie.";
    private static final String LIST_FRIEND = "user.keyboard.friend.list.";
    private static final String REMOVE_FRIEND = "user.keyboard.friend.delete.";
    private static final String MOVIE_LISTS = "user.keyboard.movie.lists.";
    private static final String FAVORITES_MOVIES = "user.keyboard.movie.lists.favorites.";
    private static final String DISLIKED_MOVIES = "user.keyboard.movie.lists.disliked.";
    private static final String MATCHES_WITH_CURRENT_FRIEND = "user.keyboard.movie.matches_friend.";
    private static final String SETTINGS = "user.keyboard.initial.settings.";
    private static final String SETTINGS_LANGUAGE = "user.keyboard.settings.language.";

    static {
        commandMapTemplate.put(ADD_FRIEND, Command.FRIEND_ADD);
        commandMapTemplate.put(RETURN, Command.RETURN_MAIN_MENU);
        commandMapTemplate.put(SELECT_FRIEND, Command.FRIEND_SELECT);
        commandMapTemplate.put(START_MATCH, Command.MOVIE_MATCH);
        commandMapTemplate.put(CONTINUE_MATCH, Command.MOVIE_MATCH);
        commandMapTemplate.put(LIKE, Command.MOVIE_LiKE);
        commandMapTemplate.put(DISLIKE, Command.MOVIE_DISLIKE);
        commandMapTemplate.put(FRIENDS_MENU, Command.FRIENDS_MENU);
        commandMapTemplate.put(MOVIE_MENU, Command.MOVIE_MENU);
        commandMapTemplate.put(LIST_FRIEND, Command.FRIENDS_LIST);
        commandMapTemplate.put(REMOVE_FRIEND, Command.FRIEND_REMOVE);
        commandMapTemplate.put(MOVIE_LISTS, Command.MOVIE_LIST);
        commandMapTemplate.put(FAVORITES_MOVIES, Command.MOVIE_FAVORITES);
        commandMapTemplate.put(DISLIKED_MOVIES, Command.MOVIE_DISLIKED);
        commandMapTemplate.put(MATCHES_WITH_CURRENT_FRIEND, Command.MOVIE_MATCHES);
        commandMapTemplate.put(SETTINGS, Command.SETTINGS);
        commandMapTemplate.put(SETTINGS_LANGUAGE, Command.LANGUAGE);
    }

    private static final Map<String, Command> commandMap = new HashMap<>();

    public Optional<Command> translateCommand(String command) {
        Optional<Command> translatedCommand;
        if (commandMap.containsKey(command)) {
            translatedCommand = Optional.of(commandMap.get(command));
        } else {
            translatedCommand = Optional.empty();
        }
        return translatedCommand;
    }
    //initial method
    private void initializeCommandMap(){
        for(String key : commandMapTemplate.keySet()){
            for(Language language : Language.values()){
                String propKey = key + language.toString().toLowerCase();
                String commandValue = new String(
                        Objects.requireNonNull(
                                env.getProperty(propKey)
                        ).getBytes(ISO_8859_1), UTF_8
                );
                commandMap.put(commandValue, commandMapTemplate.get(key));
            }
        }
        commandMap.put(INITIAL_COMMAND, Command.INITIAL);
    }
}
