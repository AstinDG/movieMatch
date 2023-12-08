package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageTemplateKeeper {

    private static final String GO_BACK_EN = "Go back";
    private static final String GO_BACK_UA = "До головного меню";
    private static final String GO_BACK_RU = "Венуться в главное меню";

    private static final String INITIAL_KEYBOARD_SETTINGS_EN = "Settings";
    private static final String INITIAL_KEYBOARD_SETTINGS_UA = "Налаштування";
    private static final String INITIAL_KEYBOARD_SETTINGS_RU = "Настройки";
    private static final String INITIAL_KEYBOARD_HELP_EN = "Help";
    private static final String INITIAL_KEYBOARD_HELP_UA = "Допомога";
    private static final String INITIAL_KEYBOARD_HELP_RU = "Помощь";
    private static final String INITIAL_KEYBOARD_FRIENDS_EN = "Friends";
    private static final String INITIAL_KEYBOARD_FRIENDS_UA = "Друзі";
    private static final String INITIAL_KEYBOARD_FRIENDS_RU = "Друзья";
    private static final String INITIAL_KEYBOARD_MOVIE_EN = "Movie";
    private static final String INITIAL_KEYBOARD_MOVIE_UA = "Фільми";
    private static final String INITIAL_KEYBOARD_MOVIE_RU = "Фильмы";

    private static final String FRIEND_KEYBOARD_ADD_EN = "Add friend";
    private static final String FRIEND_KEYBOARD_ADD_UA = "Додати друга";
    private static final String FRIEND_KEYBOARD_ADD_RU = "Добавить друга";
    private static final String FRIEND_KEYBOARD_SET_EN = "Set friend";
    private static final String FRIEND_KEYBOARD_SET_UA = "Обрати друга";
    private static final String FRIEND_KEYBOARD_SET_RU = "Выбрать друга";
    private static final String FRIEND_KEYBOARD_REMOVE_EN = "Remove friend";
    private static final String FRIEND_KEYBOARD_REMOVE_UA = "Видалити друга";
    private static final String FRIEND_KEYBOARD_REMOVE_RU = "Удалить друга";
    private static final String FRIEND_KEYBOARD_LIST_EN = "List friends";
    private static final String FRIEND_KEYBOARD_LIST_UA = "Мої друзі";
    private static final String FRIEND_KEYBOARD_LIST_RU = "Мои друзья";

    private static final String MOVIE_KEYBOARD_MY_MOVIES_EN = "My movies";
    private static final String MOVIE_KEYBOARD_MY_MOVIES_UA = "Мої фільми";
    private static final String MOVIE_KEYBOARD_MY_MOVIES_RU = "Мои фильмы";
    private static final String MOVIE_KEYBOARD_MATCHES_EN = "Matches with current friend";
    private static final String MOVIE_KEYBOARD_MATCHES_UA = "Спільні фільми з обраним другом";
    private static final String MOVIE_KEYBOARD_MATCHES_RU = "Общие фильмы с выбранным другом";
    private static final String MOVIE_KEYBOARD_START_EN = "Start match!";
    private static final String MOVIE_KEYBOARD_START_UA = "Почати підбір!";
    private static final String MOVIE_KEYBOARD_START_RU = "Начать подбор!";

    private static final String MOVIE_MATCH_KEYBOARD_LIKE_EN = "Like";
    private static final String MOVIE_MATCH_KEYBOARD_LIKE_UA = "Подобається";
    private static final String MOVIE_MATCH_KEYBOARD_LIKE_RU = "Нравится";
    private static final String MOVIE_MATCH_KEYBOARD_DISLIKE_EN = "Dislike";
    private static final String MOVIE_MATCH_KEYBOARD_DISLIKE_UA = "Не подобається";
    private static final String MOVIE_MATCH_KEYBOARD_DISLIKE_RU = "Не нравится";

    private static final String SETTINGS_KEYBOARD_LANGUAGE_EN = "Language";
    private static final String SETTINGS_KEYBOARD_LANGUAGE_UA = "Мова";
    private static final String SETTINGS_KEYBOARD_LANGUAGE_RU = "Язык";

    private static final String INVITE_FRIEND_BUTTON_VALUE_EN = "Enter Code";
    private static final String INVITE_FRIEND_BUTTON_VALUE_UA = "Введи код";
    private static final String INVITE_FRIEND_BUTTON_VALUE_RU = "Введи код";
    private static final String INVITE_FRIEND_BUTTON_CALLBACK = "code_process";

    private static final String SELECT_LANGUAGE_BUTTON_ENGLISH_UA = "Англійська";
    private static final String SELECT_LANGUAGE_BUTTON_ENGLISH_RU = "Английский";
    private static final String SELECT_LANGUAGE_BUTTON_ENGLISH_CALLBACK = "set_language_EN";
    private static final String SELECT_LANGUAGE_BUTTON_UKRAINIAN_EN = "Ukrainian";
    private static final String SELECT_LANGUAGE_BUTTON_UKRAINIAN_RU = "Украинский";
    private static final String SELECT_LANGUAGE_BUTTON_UKRAINIAN_CALLBACK = "set_language_UA";
    private static final String SELECT_LANGUAGE_BUTTON_RUSSIAN_EN = "Russian";
    private static final String SELECT_LANGUAGE_BUTTON_RUSSIAN_UA = "Російська";
    private static final String SELECT_LANGUAGE_BUTTON_RUSSIAN_CALLBACK = "set_language_RU";

    protected static final String INITIAL_KEYBOARD_KEY = "KB_INITIAL";
    private static final List<List<String>> INITIAL_KEYBOARD_EN = List.of(
            List.of(INITIAL_KEYBOARD_SETTINGS_EN, INITIAL_KEYBOARD_HELP_EN),
            List.of(INITIAL_KEYBOARD_FRIENDS_EN),
            List.of(INITIAL_KEYBOARD_MOVIE_EN)
    );
    private static final List<List<String>> INITIAL_KEYBOARD_UA = List.of(
            List.of(INITIAL_KEYBOARD_SETTINGS_UA, INITIAL_KEYBOARD_HELP_UA),
            List.of(INITIAL_KEYBOARD_FRIENDS_UA),
            List.of(INITIAL_KEYBOARD_MOVIE_UA)
    );
    private static final List<List<String>> INITIAL_KEYBOARD_RU = List.of(
            List.of(INITIAL_KEYBOARD_SETTINGS_RU, INITIAL_KEYBOARD_HELP_RU),
            List.of(INITIAL_KEYBOARD_FRIENDS_RU),
            List.of(INITIAL_KEYBOARD_MOVIE_RU)
    );

    protected static final String FRIEND_KEYBOARD_KEY = "KB_FRIEND";
    private static final List<List<String>> FRIEND_KEYBOARD_EN = List.of(
            List.of(FRIEND_KEYBOARD_ADD_EN, FRIEND_KEYBOARD_SET_EN),
            List.of(FRIEND_KEYBOARD_REMOVE_EN, FRIEND_KEYBOARD_LIST_EN),
            List.of(GO_BACK_EN)
    );
    private static final List<List<String>> FRIEND_KEYBOARD_UA = List.of(
            List.of(FRIEND_KEYBOARD_ADD_UA, FRIEND_KEYBOARD_SET_UA),
            List.of(FRIEND_KEYBOARD_REMOVE_UA, FRIEND_KEYBOARD_LIST_UA),
            List.of(GO_BACK_UA)
    );
    private static final List<List<String>> FRIEND_KEYBOARD_RU = List.of(
            List.of(FRIEND_KEYBOARD_ADD_RU, FRIEND_KEYBOARD_SET_RU),
            List.of(FRIEND_KEYBOARD_REMOVE_RU, FRIEND_KEYBOARD_LIST_RU),
            List.of(GO_BACK_RU)
    );

    protected static final String MOVIE_KEYBOARD_KEY = "KB_MOVIE";
    private static final List<List<String>> MOVIE_KEYBOARD_EN = List.of(
            List.of(GO_BACK_EN, MOVIE_KEYBOARD_MY_MOVIES_EN),
            List.of(MOVIE_KEYBOARD_MATCHES_EN),
            List.of(MOVIE_KEYBOARD_START_EN)
    );
    private static final List<List<String>> MOVIE_KEYBOARD_UA = List.of(
            List.of(GO_BACK_UA, MOVIE_KEYBOARD_MY_MOVIES_UA),
            List.of(MOVIE_KEYBOARD_MATCHES_UA),
            List.of(MOVIE_KEYBOARD_START_UA)
    );
    private static final List<List<String>> MOVIE_KEYBOARD_RU = List.of(
            List.of(GO_BACK_RU, MOVIE_KEYBOARD_MY_MOVIES_RU),
            List.of(MOVIE_KEYBOARD_MATCHES_RU),
            List.of(MOVIE_KEYBOARD_START_RU)
    );

    protected static final String SETTINGS_KEYBOARD_KEY = "KB_SETTINGS";
    private static final List<List<String>> SETTINGS_KEYBOARD_EN = List.of(
            List.of(SETTINGS_KEYBOARD_LANGUAGE_EN),
            List.of(GO_BACK_EN)
    );
    private static final List<List<String>> SETTINGS_KEYBOARD_UA = List.of(
            List.of(SETTINGS_KEYBOARD_LANGUAGE_UA),
            List.of(GO_BACK_UA)
    );
    private static final List<List<String>> SETTINGS_KEYBOARD_RU = List.of(
            List.of(SETTINGS_KEYBOARD_LANGUAGE_RU),
            List.of(GO_BACK_RU)
    );

    protected static final String MOVIE_MATCH_KEYBOARD_KEY = "KB_MOVIE_MATCH";
    private static final List<List<String>> MOVIE_MATCH_KEYBOARD_EN = List.of(
            List.of(GO_BACK_EN),
            List.of(MOVIE_MATCH_KEYBOARD_DISLIKE_EN, MOVIE_MATCH_KEYBOARD_LIKE_EN)
    );
    private static final List<List<String>> MOVIE_MATCH_KEYBOARD_UA = List.of(
            List.of(GO_BACK_UA),
            List.of(MOVIE_MATCH_KEYBOARD_DISLIKE_UA, MOVIE_MATCH_KEYBOARD_LIKE_UA)
    );
    private static final List<List<String>> MOVIE_MATCH_KEYBOARD_RU = List.of(
            List.of(GO_BACK_RU),
            List.of(MOVIE_MATCH_KEYBOARD_DISLIKE_RU, MOVIE_MATCH_KEYBOARD_LIKE_RU)
    );

    protected static final String INVITE_FRIEND_BUTTON_KEY = "BTN_INVITE";
    private static final List<Map<String, String>> INVITE_FRIEND_BUTTON_EN = List.of(
            Map.of(INVITE_FRIEND_BUTTON_VALUE_EN, INVITE_FRIEND_BUTTON_CALLBACK)
    );
    private static final List<Map<String, String>> INVITE_FRIEND_BUTTON_UA = List.of(
            Map.of(INVITE_FRIEND_BUTTON_VALUE_UA, INVITE_FRIEND_BUTTON_CALLBACK)
    );
    private static final List<Map<String, String>> INVITE_FRIEND_BUTTON_RU = List.of(
            Map.of(INVITE_FRIEND_BUTTON_VALUE_RU, INVITE_FRIEND_BUTTON_CALLBACK)
    );

    protected static final String SELECT_LANGUAGE_BUTTONS_KEY = "BTN_LANG";
    private static final List<Map<String, String>> SELECT_LANGUAGE_BUTTONS_EN = List.of(
            Map.of(SELECT_LANGUAGE_BUTTON_UKRAINIAN_EN, SELECT_LANGUAGE_BUTTON_UKRAINIAN_CALLBACK),
            Map.of(SELECT_LANGUAGE_BUTTON_RUSSIAN_EN, SELECT_LANGUAGE_BUTTON_RUSSIAN_CALLBACK)
    );
    private static final List<Map<String, String>> SELECT_LANGUAGE_BUTTONS_UA = List.of(
            Map.of(SELECT_LANGUAGE_BUTTON_RUSSIAN_UA, SELECT_LANGUAGE_BUTTON_RUSSIAN_CALLBACK),
            Map.of(SELECT_LANGUAGE_BUTTON_ENGLISH_UA, SELECT_LANGUAGE_BUTTON_ENGLISH_CALLBACK)
    );
    private static final List<Map<String, String>> SELECT_LANGUAGE_BUTTONS_RU = List.of(
            Map.of(SELECT_LANGUAGE_BUTTON_UKRAINIAN_RU, SELECT_LANGUAGE_BUTTON_UKRAINIAN_CALLBACK),
            Map.of(SELECT_LANGUAGE_BUTTON_ENGLISH_RU, SELECT_LANGUAGE_BUTTON_ENGLISH_CALLBACK)
    );

    protected static final String INITIAL_KEY = "INITIAL";
    private static final String INITIAL_TEMPLATE_EN = "Hi, %s!\nYour current friend: %s\nTotal friends: %d\n\nLet`s start!";
    private static final String INITIAL_TEMPLATE_UA = "Привіт, %s!\nТвій друг для вибору: %s\nВсього друзів: %d\n\nПочинай скоріше!";
    private static final String INITIAL_TEMPLATE_RU = "Привет, %s!\nТвой друг для выбора: %s\nВсего друзей: %d\n\nДавай начнем!";

    protected static final String SELECT_OPTION_KEY = "OPT_TXT";
    private static final String SELECT_OPTION_TEXT_EN = "Select option";
    private static final String SELECT_OPTION_TEXT_UA = "Обери варіант";
    private static final String SELECT_OPTION_TEXT_RU = "Выбери вариант";

    protected static final String INVITE_FRIEND_KEY = "FR_INVITE";
    private static final String INVITE_FRIEND_TEMPLATE_EN = "Your invite code: %s (available within %d minutes)\n\nSend this code to your friend or click the button below to enter the code!";
    private static final String INVITE_FRIEND_TEMPLATE_UA = "Твій код запрошення: %s (доступний протягом %d хвилин)\n\nВідправ цей код своєму приятелю, або натисни кнопку нижче щоб ввести код!";
    private static final String INVITE_FRIEND_TEMPLATE_RU = "Твой код приглшения: %s (доступен %d минут)\n\nОтправь этот код своему другу, или нажми кнопку ниже, что бы ввести код!";

    protected static final String SELECT_FRIEND_KEY = "FR_SELECT";
    private static final String SELECT_FRIEND_MESSAGE_EN = "Select a friend with whom you will search for a movie";
    private static final String SELECT_FRIEND_MESSAGE_UA = "Обери друга, з яким обираєш фільм";
    private static final String SELECT_FRIEND_MESSAGE_RU = "Выбери друга, с которым выбираешь фильм";

    protected static final String FRIEND_SELECTED_KEY = "FR_SELECTED";
    private static final String FRIEND_SELECTED_EN = "Friend %s is selected successfully!";
    private static final String FRIEND_SELECTED_UA = "%s обриний успішно!";
    private static final String FRIEND_SELECTED_RU = "%s выбран успешно!";

    protected static final String SELECT_FRIEND_TO_DELETE_KEY = "FR_DELETE";
    private static final String SELECT_FRIEND_TO_DELETE_MESSAGE_EN = "Select friend to remove:";
    private static final String SELECT_FRIEND_TO_DELETE_MESSAGE_UA = "Обери друга, якого хочешь видалити:";
    private static final String SELECT_FRIEND_TO_DELETE_MESSAGE_RU = "Выбери друга, которого хочешь удалить:";

    protected static final String NOTIFY_NEW_FRIEND_KEY = "FR_NOTiFY_NEW";
    private static final String NOTIFY_NEW_FRIEND_TEMPLATE_EN = "%s is your friend now :)";
    private static final String NOTIFY_NEW_FRIEND_TEMPLATE_UA = "%s тепер Ваш друг :)";
    private static final String NOTIFY_NEW_FRIEND_TEMPLATE_RU = "%s теперь твой друг :)";

    protected static final String MOVIE_MESSAGE_KEY = "MV";
    private static final String MOVIE_MESSAGE_TEMPLATE_EN = "**%s**\nGenre: %s\nyear of release: %d\ndesc:%s";
    private static final String MOVIE_MESSAGE_TEMPLATE_UA = "**%s**\nЖанр: %s\nРік: %d\nОпис:%s";
    private static final String MOVIE_MESSAGE_TEMPLATE_RU = "**%s**\nЖанр: %s\nГод: %d\nОписание:%s";

    protected static final String FAVORITE_MOVIE_EMPTY_KEY = "MV_FV_EMPTY";
    private static final String FAVORITE_MOVIES_EMPTY_MESSAGE_EN = "You don't have favorite movies";
    private static final String FAVORITE_MOVIES_EMPTY_MESSAGE_UA = "У Вас ще намає улюблених фільмів";
    private static final String FAVORITE_MOVIES_EMPTY_MESSAGE_RU = "У Вас еще нет любимых фильмов";

    protected static final String FAVORITE_MOVIES_KEY = "MV_FV_TEMPLATE";
    private static final String FAVORITE_MOVIES_TEMPLATE_EN = "%d. %s\ngenre: %s\nyear: %d\n\n";
    private static final String FAVORITE_MOVIES_TEMPLATE_UA = "%d. %s\nжанр: %s\nрік: %d\n\n";
    private static final String FAVORITE_MOVIES_TEMPLATE_RU = "%d. %s\nжанр: %s\nгод: %d\n\n";

    protected static final String NO_NEW_MOVIES_KEY = "MV_NO_NEW";
    private static final String NO_NEW_MOVIES_EN = "No new movies for you yet :(";
    private static final String NO_NEW_MOVIES_UA = "Для тебе немає нових фільмів :(";
    private static final String NO_NEW_MOVIES_RU = "Для тебя нет новых фильмов :(";

    protected static final String SELECT_LANGUAGE_KEY = "LANG_SET";
    private static final String SELECT_LANGUAGE_MESSAGE_EN = "Select language:";
    private static final String SELECT_LANGUAGE_MESSAGE_UA = "Обери мову:";
    private static final String SELECT_LANGUAGE_MESSAGE_RU = "Выбери язык:";

    protected static final String ERROR_MESSAGE_UNKNOWN_COMMAND_KEY = "ERR_UNK_CMD";
    private static final String ERROR_MESSAGE_UNKNOWN_COMMAND_EN = "Unknown command!";
    private static final String ERROR_MESSAGE_UNKNOWN_COMMAND_UA = "Невідома команда!";
    private static final String ERROR_MESSAGE_UNKNOWN_COMMAND_RU = "Неизвестная команда!";

    protected static final String ERROR_MESSAGE_SETTING_FRIEND_KEY = "ERR_SET_FR";
    private static final String ERROR_MESSAGE_SETTING_FRIEND_EN = "Something went wrong til setting a friend :(";
    private static final String ERROR_MESSAGE_SETTING_FRIEND_UA = "Щось пішло не так при виборі друга :(";
    private static final String ERROR_MESSAGE_SETTING_FRIEND_RU = "Что-то пошло не так при выборе друга :(";

    protected static final String ERROR_MESSAGE_FRIEND_NOT_SELECTED_KEY = "ERR_FR_NOT_SELECTED";
    private static final String ERROR_MESSAGE_FRIEND_NOT_SELECTED_EN = "You didn't choose a friend for selection";
    private static final String ERROR_MESSAGE_FRIEND_NOT_SELECTED_UA = "Спочатку обери друга";
    private static final String ERROR_MESSAGE_FRIEND_NOT_SELECTED_RU = "Сначала выбери друга";

    private static final Map<String, Map<Language, String>> MESSAGES_AND_TEMPLATES = new HashMap<>();
    private static final Map<String, Map<Language, List<List<String>>>> KEYBOARDS = new HashMap<>();
    private static final Map<String, Map<Language, List<Map<String, String>>>> BUTTONS = new HashMap<>();

    //translate for MessageBuilder.
    static {
        MESSAGES_AND_TEMPLATES.put(INITIAL_KEY,
                Map.of(
                        Language.EN, INITIAL_TEMPLATE_EN,
                        Language.UA, INITIAL_TEMPLATE_UA,
                        Language.RU, INITIAL_TEMPLATE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(SELECT_OPTION_KEY,
                Map.of(
                        Language.EN, SELECT_OPTION_TEXT_EN,
                        Language.UA, SELECT_OPTION_TEXT_UA,
                        Language.RU, SELECT_OPTION_TEXT_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(INVITE_FRIEND_KEY,
                Map.of(
                        Language.EN, INVITE_FRIEND_TEMPLATE_EN,
                        Language.UA, INVITE_FRIEND_TEMPLATE_UA,
                        Language.RU, INVITE_FRIEND_TEMPLATE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(SELECT_FRIEND_KEY,
                Map.of(
                        Language.EN, SELECT_FRIEND_MESSAGE_EN,
                        Language.UA, SELECT_FRIEND_MESSAGE_UA,
                        Language.RU, SELECT_FRIEND_MESSAGE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(SELECT_FRIEND_TO_DELETE_KEY,
                Map.of(
                        Language.EN, SELECT_FRIEND_TO_DELETE_MESSAGE_EN,
                        Language.UA, SELECT_FRIEND_TO_DELETE_MESSAGE_UA,
                        Language.RU, SELECT_FRIEND_TO_DELETE_MESSAGE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(NOTIFY_NEW_FRIEND_KEY,
                Map.of(
                        Language.EN, NOTIFY_NEW_FRIEND_TEMPLATE_EN,
                        Language.UA, NOTIFY_NEW_FRIEND_TEMPLATE_UA,
                        Language.RU, NOTIFY_NEW_FRIEND_TEMPLATE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(MOVIE_MESSAGE_KEY,
                Map.of(
                        Language.EN, MOVIE_MESSAGE_TEMPLATE_EN,
                        Language.UA, MOVIE_MESSAGE_TEMPLATE_UA,
                        Language.RU, MOVIE_MESSAGE_TEMPLATE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(FAVORITE_MOVIE_EMPTY_KEY,
                Map.of(
                        Language.EN, FAVORITE_MOVIES_EMPTY_MESSAGE_EN,
                        Language.UA, FAVORITE_MOVIES_EMPTY_MESSAGE_UA,
                        Language.RU, FAVORITE_MOVIES_EMPTY_MESSAGE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(FAVORITE_MOVIES_KEY,
                Map.of(
                        Language.EN, FAVORITE_MOVIES_TEMPLATE_EN,
                        Language.UA, FAVORITE_MOVIES_TEMPLATE_UA,
                        Language.RU, FAVORITE_MOVIES_TEMPLATE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(SELECT_LANGUAGE_KEY,
                Map.of(
                        Language.EN, SELECT_LANGUAGE_MESSAGE_EN,
                        Language.UA, SELECT_LANGUAGE_MESSAGE_UA,
                        Language.RU, SELECT_LANGUAGE_MESSAGE_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(ERROR_MESSAGE_UNKNOWN_COMMAND_KEY,
                Map.of(
                        Language.EN, ERROR_MESSAGE_UNKNOWN_COMMAND_EN,
                        Language.UA, ERROR_MESSAGE_UNKNOWN_COMMAND_UA,
                        Language.RU, ERROR_MESSAGE_UNKNOWN_COMMAND_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(FRIEND_SELECTED_KEY,
                Map.of(
                        Language.EN, FRIEND_SELECTED_EN,
                        Language.UA, FRIEND_SELECTED_UA,
                        Language.RU, FRIEND_SELECTED_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(ERROR_MESSAGE_SETTING_FRIEND_KEY,
                Map.of(
                        Language.EN, ERROR_MESSAGE_SETTING_FRIEND_EN,
                        Language.UA, ERROR_MESSAGE_SETTING_FRIEND_UA,
                        Language.RU, ERROR_MESSAGE_SETTING_FRIEND_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(ERROR_MESSAGE_FRIEND_NOT_SELECTED_KEY,
                Map.of(
                        Language.EN, ERROR_MESSAGE_FRIEND_NOT_SELECTED_EN,
                        Language.UA, ERROR_MESSAGE_FRIEND_NOT_SELECTED_UA,
                        Language.RU, ERROR_MESSAGE_FRIEND_NOT_SELECTED_RU
                )
        );
        MESSAGES_AND_TEMPLATES.put(NO_NEW_MOVIES_KEY,
                Map.of(
                        Language.EN, NO_NEW_MOVIES_EN,
                        Language.UA, NO_NEW_MOVIES_UA,
                        Language.RU, NO_NEW_MOVIES_RU
                )
        );
    }

    static {
        KEYBOARDS.put(INITIAL_KEYBOARD_KEY,
                Map.of(
                        Language.EN, INITIAL_KEYBOARD_EN,
                        Language.UA, INITIAL_KEYBOARD_UA,
                        Language.RU, INITIAL_KEYBOARD_RU
                )
        );
        KEYBOARDS.put(FRIEND_KEYBOARD_KEY,
                Map.of(
                        Language.EN, FRIEND_KEYBOARD_EN,
                        Language.UA, FRIEND_KEYBOARD_UA,
                        Language.RU, FRIEND_KEYBOARD_RU
                )
        );
        KEYBOARDS.put(MOVIE_KEYBOARD_KEY,
                Map.of(
                        Language.EN, MOVIE_KEYBOARD_EN,
                        Language.UA, MOVIE_KEYBOARD_UA,
                        Language.RU, MOVIE_KEYBOARD_RU
                )
        );
        KEYBOARDS.put(MOVIE_MATCH_KEYBOARD_KEY,
                Map.of(
                        Language.EN, MOVIE_MATCH_KEYBOARD_EN,
                        Language.UA, MOVIE_MATCH_KEYBOARD_UA,
                        Language.RU, MOVIE_MATCH_KEYBOARD_RU
                )
        );
        KEYBOARDS.put(SETTINGS_KEYBOARD_KEY,
                Map.of(
                        Language.EN, SETTINGS_KEYBOARD_EN,
                        Language.UA, SETTINGS_KEYBOARD_UA,
                        Language.RU, SETTINGS_KEYBOARD_RU
                )
        );
    }

    static {
        BUTTONS.put(INVITE_FRIEND_BUTTON_KEY,
                Map.of(
                        Language.EN, INVITE_FRIEND_BUTTON_EN,
                        Language.UA, INVITE_FRIEND_BUTTON_UA,
                        Language.RU, INVITE_FRIEND_BUTTON_RU
                )
        );
        BUTTONS.put(SELECT_LANGUAGE_BUTTONS_KEY,
                Map.of(
                        Language.EN, SELECT_LANGUAGE_BUTTONS_EN,
                        Language.UA, SELECT_LANGUAGE_BUTTONS_UA,
                        Language.RU, SELECT_LANGUAGE_BUTTONS_RU
                )
        );
    }

    public static String getMessageOrTemplateByKey(String key, Language language) {
        return MESSAGES_AND_TEMPLATES.get(key).get(language);
    }

    public static List<List<String>> getKeyboardByKey(String key, Language language) {
        return KEYBOARDS.get(key).get(language);
    }

    public static List<Map<String, String>> getButtonsByKey(String key, Language language) {
        return BUTTONS.get(key).get(language);
    }
}
