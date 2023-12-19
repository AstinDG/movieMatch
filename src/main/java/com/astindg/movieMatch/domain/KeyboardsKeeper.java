package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import org.springframework.core.env.Environment;

import java.util.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class KeyboardsKeeper {
    private static final String KEYBOARDS_PROPERTY_KEY = "user.keyboard.";
    private static final String RETURN_BUTTON_KEY = "user.keyboard.button.return_main";
    private static final Map<String, List<List<String>>> KEYBOARDS_TEMPLATES = new HashMap<>();

    static {
        List<List<String>> initialKeyboardTemplate = new ArrayList<>(List.of(
                List.of("settings", "help"),
                List.of("friends"),
                List.of("movie")
        ));

        List<List<String>> friendKeyboardTemplate = new ArrayList<>(List.of(
                List.of("add", "set"),
                List.of("delete", "list"),
                List.of(RETURN_BUTTON_KEY)
        ));

        List<List<String>> movieKeyboardTemplate = new ArrayList<>(List.of(
                List.of(RETURN_BUTTON_KEY, "my"),
                List.of("matches_friend"),
                List.of("start_match")
        ));

        List<List<String>> matchKeyboardTemplate = new ArrayList<>(List.of(
                List.of(RETURN_BUTTON_KEY),
                List.of("like", "dislike")
        ));

        List<List<String>> settingsKeyboardTemplate = new ArrayList<>(List.of(
                List.of("language"),
                List.of(RETURN_BUTTON_KEY)
        ));

        KEYBOARDS_TEMPLATES.put("initial", initialKeyboardTemplate);
        KEYBOARDS_TEMPLATES.put("friend", friendKeyboardTemplate);
        KEYBOARDS_TEMPLATES.put("movie", movieKeyboardTemplate);
        KEYBOARDS_TEMPLATES.put("match", matchKeyboardTemplate);
        KEYBOARDS_TEMPLATES.put("settings", settingsKeyboardTemplate);
    }

    private final Environment env;
    private final Map<String, Map<Language, List<List<String>>>> keyboards = new HashMap<>();

    public KeyboardsKeeper(Environment env) {
        this.env = env;
    }

    public List<List<String>> getKeyboard(String key, Language language) {
        return keyboards.get(key).get(language);
    }

    public void initializeKeyboardsMap(){
        for (String key : KEYBOARDS_TEMPLATES.keySet()) {
            Map<Language, List<List<String>>> mapKeyboardByLang = new HashMap<>();

            for (Language language : Language.values()) {
                List<List<String>> keyboard = getKeyboard(language, key);
                mapKeyboardByLang.put(language, keyboard);
            }
            keyboards.put(key, mapKeyboardByLang);
        }
    }

    private List<List<String>> getKeyboard(Language language, String key){
        List<List<String>> keyboard = new ArrayList<>();

        String keyboardPropertyKey = KEYBOARDS_PROPERTY_KEY + key;

        for (List<String> keyRow : KEYBOARDS_TEMPLATES.get(key)) {
            List<String> buttons = getButtonsRow(keyRow, keyboardPropertyKey, language);
            keyboard.add(buttons);
        }
        return keyboard;
    }

    private List<String> getButtonsRow(List<String> keyRow, String keyboardPropertyKey, Language language){
        List<String> buttons = new ArrayList<>();

        for (String buttonKey : keyRow) {

            String buttonPropertyKey;
            if (buttonKey.endsWith("return_main")) {
                buttonPropertyKey = buttonKey;
            } else {
                buttonPropertyKey = keyboardPropertyKey + '.' + buttonKey;
            }
            buttonPropertyKey += '.' + language.toString().toLowerCase();

            String buttonValue = new String(
                    Objects.requireNonNull(
                            env.getProperty(buttonPropertyKey)
                    ).getBytes(ISO_8859_1), UTF_8
            );

            buttons.add(buttonValue);
        }
        return buttons;
    }
}
