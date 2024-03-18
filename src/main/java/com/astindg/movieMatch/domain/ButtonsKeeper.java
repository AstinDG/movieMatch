package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import org.springframework.core.env.Environment;

import java.util.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public record ButtonsKeeper(Environment env) {

    private static final String BUTTONS_PROPERTY_KEY = "user.button.";
    private static final String INVITE_BUTTON_KEY = "friend.invite_code.";
    private static final String LANGUAGE_BUTTON_KEY = "settings.select_language.";
    private static final String MOVIE_REMOVE_FAVORITE_KEY = "movie.remove.favorite.";
    private static final String MOVIE_REMOVE_DISLIKED_KEY = "movie.remove.disliked.";
    private static final String CALLBACK_KEY = "callback";
    private static final Map<String, Map<Language, List<Map<String, String>>>> BUTTONS = new HashMap<>();

    public List<Map<String, String>> getButton(String key, Language language) {
        return BUTTONS.get(key).get(language);
    }

    //initial method
    public void initializeButtons() {
        initializeInviteButtons();
        initializeLanguageButtons();
        initializeRemoveFromListsButtons();
    }

    private void initializeInviteButtons() {
        Map<Language, List<Map<String, String>>> inviteButtons = new HashMap<>();
        String buttonPropertyKey = BUTTONS_PROPERTY_KEY + INVITE_BUTTON_KEY;

        String buttonCallbackKey = buttonPropertyKey + CALLBACK_KEY;
        String buttonCallback = getStringFromProperty(buttonCallbackKey);

        for (Language language : Language.values()) {
            List<Map<String, String>> button = new ArrayList<>();

            String buttonValueKey = buttonPropertyKey + language.toString().toLowerCase();
            String buttonValue = getStringFromProperty(buttonValueKey);
            button.add(Map.of(buttonValue, buttonCallback));

            inviteButtons.put(language, button);
        }

        BUTTONS.put(INVITE_BUTTON_KEY, inviteButtons);
    }

    private void initializeLanguageButtons() {
        Map<Language, List<Map<String, String>>> languageButtons = new HashMap<>();
        String buttonPropertyKey = BUTTONS_PROPERTY_KEY + LANGUAGE_BUTTON_KEY;

        for (Language language : Language.values()) {
            List<Map<String, String>> button = new ArrayList<>();
            for (Language languageKey : Language.values()) {
                if (!languageKey.equals(language)) {
                    String languagePropertyKey = switch (languageKey) {
                        case EN -> "english";
                        case UA -> "ukrainian";
                        case RU -> "russian";
                    };
                    languagePropertyKey = buttonPropertyKey + languagePropertyKey + '.';

                    String languageCallbackKey = languagePropertyKey + CALLBACK_KEY;
                    languagePropertyKey = languagePropertyKey + language.toString().toLowerCase();

                    String languageButtonValue = getStringFromProperty(languagePropertyKey);
                    String languageCallback = getStringFromProperty(languageCallbackKey);

                    button.add(Map.of(languageButtonValue, languageCallback));
                }
            }
            languageButtons.put(language, button);
        }
        BUTTONS.put(LANGUAGE_BUTTON_KEY, languageButtons);
    }

    private void initializeRemoveFromListsButtons(){
        Map<Language, List<Map<String, String>>> removeFavoriteButtons = new HashMap<>();
        Map<Language, List<Map<String, String>>> removeDislikedButtons = new HashMap<>();

        String removeFavoriteBtnProp = BUTTONS_PROPERTY_KEY + MOVIE_REMOVE_FAVORITE_KEY;
        String removeFavoriteBtnCallback = getStringFromProperty(removeFavoriteBtnProp + CALLBACK_KEY);
        String removeDislikedBtnProp = BUTTONS_PROPERTY_KEY + MOVIE_REMOVE_DISLIKED_KEY;
        String removeDislikedBtnCallback = getStringFromProperty(removeDislikedBtnProp + CALLBACK_KEY);

        for(Language language : Language.values()){
            String lang = language.toString().toLowerCase();

            List<Map<String, String>> removeFavoriteBtn = new ArrayList<>();
            String removeFavoriteBtnValue = getStringFromProperty(removeFavoriteBtnProp + lang);
            removeFavoriteBtn.add(Map.of(removeFavoriteBtnValue, removeFavoriteBtnCallback));

            removeFavoriteButtons.put(language, removeFavoriteBtn);

            List<Map<String, String>> removeDislikedBtn = new ArrayList<>();
            String removeDislikedBtnValue = getStringFromProperty(removeDislikedBtnProp + lang);
            removeDislikedBtn.add(Map.of(removeDislikedBtnValue, removeDislikedBtnCallback));

            removeDislikedButtons.put(language, removeDislikedBtn);
        }

        BUTTONS.put(MOVIE_REMOVE_FAVORITE_KEY, removeFavoriteButtons);
        BUTTONS.put(MOVIE_REMOVE_DISLIKED_KEY, removeDislikedButtons);
    }

    private String getStringFromProperty(String key){
        return new String(Objects.requireNonNull(env.getProperty(key)).getBytes(ISO_8859_1), UTF_8);
    }
}
