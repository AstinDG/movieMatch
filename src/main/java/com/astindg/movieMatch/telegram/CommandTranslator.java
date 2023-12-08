package com.astindg.movieMatch.telegram;

import com.astindg.movieMatch.domain.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandTranslator {
    private static final Map<String, Command> commandMap = new HashMap<>();

    private static final String INITIAL_COMMAND = "/start";
    private static final String ADD_FRIEND_COMMAND_EN = "Add friend";
    private static final String ADD_FRIEND_COMMAND_UA = "Додати друга";
    private static final String ADD_FRIEND_COMMAND_RU = "Добавить друга";
    private static final String RETURN_COMMAND_EN = "Go back";
    private static final String RETURN_COMMAND_UA = "До головного меню";
    private static final String RETURN_COMMAND_RU = "Венуться в главное меню";
    private static final String SELECT_FRIEND_COMMAND_EN = "Set friend";
    private static final String SELECT_FRIEND_COMMAND_UA = "Обрати друга";
    private static final String SELECT_FRIEND_COMMAND_RU = "Выбрать друга";
    private static final String START_MATCH_COMMAND_EN = "Start match!";
    private static final String START_MATCH_COMMAND_UA = "Почати підбір!";
    private static final String START_MATCH_COMMAND_RU = "Начать подбор!";
    private static final String LIKE_COMMAND_EN = "Like";
    private static final String LIKE_COMMAND_UA = "Подобається";
    private static final String LIKE_COMMAND_RU = "Нравится";
    private static final String DISLIKE_COMMAND_EN = "Dislike";
    private static final String DISLIKE_COMMAND_UA = "Не подобається";
    private static final String DISLIKE_COMMAND_RU = "Не нравится";
    private static final String FRIENDS_MENU_COMMAND_EN = "Friends";
    private static final String FRIENDS_MENU_COMMAND_UA = "Друзі";
    private static final String FRIENDS_MENU_COMMAND_RU = "Друзья";
    private static final String MOVIE_MENU_COMMAND_EN = "Movie";
    private static final String MOVIE_MENU_COMMAND_UA = "Фільми";
    private static final String MOVIE_MENU_COMMAND_RU = "Фильмы";
    private static final String LIST_FRIEND_COMMAND_EN = "List friends";
    private static final String LIST_FRIEND_COMMAND_UA = "Мої друзі";
    private static final String LIST_FRIEND_COMMAND_RU = "Мои друзья";
    private static final String REMOVE_FRIEND_COMMAND_EN = "Remove friend";
    private static final String REMOVE_FRIEND_COMMAND_UA = "Видалити друга";
    private static final String REMOVE_FRIEND_COMMAND_RU = "Удалить друга";
    private static final String FAVORITES_MOVIES_COMMAND_EN = "My movies";
    private static final String FAVORITES_MOVIES_COMMAND_UA = "Мої фільми";
    private static final String FAVORITES_MOVIES_COMMAND_RU = "Мои фильмы";
    private static final String SETTINGS_COMMAND_EN = "Settings";
    private static final String SETTINGS_COMMAND_UA = "Налаштування";
    private static final String SETTINGS_COMMAND_RU = "Настройки";
    private static final String SETTINGS_LANGUAGE_COMMAND_EN = "Language";
    private static final String SETTINGS_LANGUAGE_COMMAND_UA = "Мова";
    private static final String SETTINGS_LANGUAGE_COMMAND_RU = "Язык";

    static {
        commandMap.put(INITIAL_COMMAND, Command.INITIAL);
        commandMap.put(ADD_FRIEND_COMMAND_EN, Command.FRIEND_ADD);
        commandMap.put(ADD_FRIEND_COMMAND_UA, Command.FRIEND_ADD);
        commandMap.put(ADD_FRIEND_COMMAND_RU, Command.FRIEND_ADD);
        commandMap.put(RETURN_COMMAND_EN, Command.RETURN_MAIN_MENU);
        commandMap.put(RETURN_COMMAND_UA, Command.RETURN_MAIN_MENU);
        commandMap.put(RETURN_COMMAND_RU, Command.RETURN_MAIN_MENU);
        commandMap.put(SELECT_FRIEND_COMMAND_EN, Command.FRIEND_SELECT);
        commandMap.put(SELECT_FRIEND_COMMAND_UA, Command.FRIEND_SELECT);
        commandMap.put(SELECT_FRIEND_COMMAND_RU, Command.FRIEND_SELECT);
        commandMap.put(START_MATCH_COMMAND_EN, Command.MOVIE_MATCH);
        commandMap.put(START_MATCH_COMMAND_UA, Command.MOVIE_MATCH);
        commandMap.put(START_MATCH_COMMAND_RU, Command.MOVIE_MATCH);
        commandMap.put(LIKE_COMMAND_EN, Command.MOVIE_LiKE);
        commandMap.put(LIKE_COMMAND_UA, Command.MOVIE_LiKE);
        commandMap.put(LIKE_COMMAND_RU, Command.MOVIE_LiKE);
        commandMap.put(DISLIKE_COMMAND_EN, Command.MOVIE_DISLIKE);
        commandMap.put(DISLIKE_COMMAND_UA, Command.MOVIE_DISLIKE);
        commandMap.put(DISLIKE_COMMAND_RU, Command.MOVIE_DISLIKE);
        commandMap.put(FRIENDS_MENU_COMMAND_EN, Command.FRIENDS_MENU);
        commandMap.put(FRIENDS_MENU_COMMAND_UA, Command.FRIENDS_MENU);
        commandMap.put(FRIENDS_MENU_COMMAND_RU, Command.FRIENDS_MENU);
        commandMap.put(MOVIE_MENU_COMMAND_EN, Command.MOVIE_MENU);
        commandMap.put(MOVIE_MENU_COMMAND_UA, Command.MOVIE_MENU);
        commandMap.put(MOVIE_MENU_COMMAND_RU, Command.MOVIE_MENU);
        commandMap.put(LIST_FRIEND_COMMAND_EN, Command.FRIENDS_LIST);
        commandMap.put(LIST_FRIEND_COMMAND_UA, Command.FRIENDS_LIST);
        commandMap.put(LIST_FRIEND_COMMAND_RU, Command.FRIENDS_LIST);
        commandMap.put(REMOVE_FRIEND_COMMAND_EN, Command.FRIEND_REMOVE);
        commandMap.put(REMOVE_FRIEND_COMMAND_UA, Command.FRIEND_REMOVE);
        commandMap.put(REMOVE_FRIEND_COMMAND_RU, Command.FRIEND_REMOVE);
        commandMap.put(FAVORITES_MOVIES_COMMAND_EN, Command.MOVIE_FAVORITES);
        commandMap.put(FAVORITES_MOVIES_COMMAND_UA, Command.MOVIE_FAVORITES);
        commandMap.put(FAVORITES_MOVIES_COMMAND_RU, Command.MOVIE_FAVORITES);
        commandMap.put(SETTINGS_COMMAND_EN, Command.SETTINGS);
        commandMap.put(SETTINGS_COMMAND_UA, Command.SETTINGS);
        commandMap.put(SETTINGS_COMMAND_RU, Command.SETTINGS);
        commandMap.put(SETTINGS_LANGUAGE_COMMAND_EN, Command.LANGUAGE);
        commandMap.put(SETTINGS_LANGUAGE_COMMAND_UA, Command.LANGUAGE);
        commandMap.put(SETTINGS_LANGUAGE_COMMAND_RU, Command.LANGUAGE);
    }

    protected static Optional<Command> translateCommand(String command) {
        Optional<Command> translatedCommand;
        if (commandMap.containsKey(command)) {
            translatedCommand = Optional.of(commandMap.get(command));
        } else {
            translatedCommand = Optional.empty();
        }
        return translatedCommand;
    }
}
