package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import org.apache.commons.lang3.tuple.Pair;
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
    private static final String SWITCH_BUTTONS = "switch_page.";
    private static final String SWITCH_BUTTON_PREV_KEY = "switch_page.previous.";
    private static final String SWITCH_BUTTON_NEXT_KEY = "switch_page.next.";
    private static final String CALLBACK_KEY = "callback";
    private static final Map<String, Map<Language, List<List<Pair<String, String>>>>> BUTTONS = new HashMap<>();

    public List<List<Pair<String, String>>> getButton(String key, Language language) {
        return BUTTONS.get(key).get(language);
    }

    //initial method
    public void initializeButtons() {
        initializeInviteButtons();
        initializeLanguageButtons();
        initializeRemoveFromListsButtons();
        initializeSwitchButtons();
    }

    private void initializeInviteButtons() {
        Map<Language, List<List<Pair<String, String>>>> inviteButtons = new HashMap<>();
        String buttonPropertyKey = BUTTONS_PROPERTY_KEY + INVITE_BUTTON_KEY;

        String buttonCallbackKey = buttonPropertyKey + CALLBACK_KEY;
        String buttonCallback = getStringFromProperty(buttonCallbackKey);

        for (Language language : Language.values()) {
            List<List<Pair<String, String>>> buttons = new ArrayList<>();

            String buttonValueKey = buttonPropertyKey + language.toString().toLowerCase();
            String buttonValue = getStringFromProperty(buttonValueKey);
            buttons.add(List.of(Pair.of(buttonValue, buttonCallback)));

            inviteButtons.put(language, buttons);
        }

        BUTTONS.put(INVITE_BUTTON_KEY, inviteButtons);
    }

    private void initializeLanguageButtons() {
        Map<Language, List<List<Pair<String, String>>>> languageButtons = new HashMap<>();
        String buttonPropertyKey = BUTTONS_PROPERTY_KEY + LANGUAGE_BUTTON_KEY;

        for (Language language : Language.values()) {
            List<List<Pair<String, String>>> buttons = new ArrayList<>();
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

                    buttons.add(List.of(Pair.of(languageButtonValue, languageCallback)));
                }
            }
            languageButtons.put(language, buttons);
        }
        BUTTONS.put(LANGUAGE_BUTTON_KEY, languageButtons);
    }

    private void initializeRemoveFromListsButtons(){
        Map<Language, List<List<Pair<String, String>>>> removeFavoriteButtons = new HashMap<>();
        Map<Language, List<List<Pair<String, String>>>> removeDislikedButtons = new HashMap<>();

        String removeFavoriteBtnProp = BUTTONS_PROPERTY_KEY + MOVIE_REMOVE_FAVORITE_KEY;
        String removeFavoriteBtnCallback = getStringFromProperty(removeFavoriteBtnProp + CALLBACK_KEY);
        String removeDislikedBtnProp = BUTTONS_PROPERTY_KEY + MOVIE_REMOVE_DISLIKED_KEY;
        String removeDislikedBtnCallback = getStringFromProperty(removeDislikedBtnProp + CALLBACK_KEY);

        for(Language language : Language.values()){
            String lang = language.toString().toLowerCase();

            List<List<Pair<String, String>>> removeFavoriteBtn = new ArrayList<>();
            String removeFavoriteBtnValue = getStringFromProperty(removeFavoriteBtnProp + lang);
            removeFavoriteBtn.add(List.of(Pair.of(removeFavoriteBtnValue, removeFavoriteBtnCallback)));

            removeFavoriteButtons.put(language, removeFavoriteBtn);

            List<List<Pair<String, String>>> removeDislikedBtn = new ArrayList<>();
            String removeDislikedBtnValue = getStringFromProperty(removeDislikedBtnProp + lang);
            removeDislikedBtn.add(List.of(Pair.of(removeDislikedBtnValue, removeDislikedBtnCallback)));

            removeDislikedButtons.put(language, removeDislikedBtn);
        }

        BUTTONS.put(MOVIE_REMOVE_FAVORITE_KEY, removeFavoriteButtons);
        BUTTONS.put(MOVIE_REMOVE_DISLIKED_KEY, removeDislikedButtons);
    }

    private void initializeSwitchButtons(){
        Map<Language, List<List<Pair<String, String>>>> switchButtonPrevious = new HashMap<>();
        Map<Language, List<List<Pair<String, String>>>> switchButtonNext = new HashMap<>();
        String previousButtonKey = BUTTONS_PROPERTY_KEY + SWITCH_BUTTON_PREV_KEY;
        String nextButtonKey = BUTTONS_PROPERTY_KEY + SWITCH_BUTTON_NEXT_KEY;
        String switchButtonCallback = getStringFromProperty(BUTTONS_PROPERTY_KEY + SWITCH_BUTTONS + CALLBACK_KEY);

        for(Language language : Language.values()){
            String previousButtonValue = getStringFromProperty(previousButtonKey + language.toString().toLowerCase());
            String nextButtonValue = getStringFromProperty(nextButtonKey + language.toString().toLowerCase());

            switchButtonPrevious.put(language, List.of(List.of(Pair.of(previousButtonValue, switchButtonCallback))));
            switchButtonNext.put(language, List.of(List.of(Pair.of(nextButtonValue, switchButtonCallback))));
        }

        BUTTONS.put(SWITCH_BUTTON_PREV_KEY, switchButtonPrevious);
        BUTTONS.put(SWITCH_BUTTON_NEXT_KEY, switchButtonNext);
    }

    private String getStringFromProperty(String key){
        return new String(Objects.requireNonNull(env.getProperty(key)).getBytes(ISO_8859_1), UTF_8);
    }
}
