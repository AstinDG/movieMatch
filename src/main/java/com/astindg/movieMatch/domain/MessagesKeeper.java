package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import org.springframework.core.env.Environment;

import java.util.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public record MessagesKeeper(Environment env) {

    private static final Map<String, Map<Language, String>> messages = new HashMap<>();
    private static final String MESSAGES_PROPERTY_KEY = "user.message.";
    private static final List<String> keys = new ArrayList<>();

    static {
        keys.add("initial");
        keys.add("select_option");
        keys.add("friend.invite");
        keys.add("friend.select");
        keys.add("friend.selected");
        keys.add("friend.delete");
        keys.add("friend.new");
        keys.add("friend.error.set_friend");
        keys.add("movie");
        keys.add("movie.favorite");
        keys.add("movie.error.empty.list");
        keys.add("movie.error.empty.favorite");
        keys.add("movie.error.friend_not_selected");
        keys.add("settings.select_language");
        keys.add("error.unknown_command");
    }

    public String getMessage(String key, Language language) {
        return messages.get(key).get(language);
    }
    //initial method
    private void initializeMessages(){
        for (String key : keys) {
            String message_key = MESSAGES_PROPERTY_KEY + key;

            Map<Language, String> map = new HashMap<>();

            for (Language language : Language.values()) {

                String propertyKey = message_key +
                        '.' +
                        language.toString().toLowerCase();

                String message = new String(
                        Objects.requireNonNull(env.getProperty(propertyKey)).getBytes(ISO_8859_1),
                        UTF_8
                );
                map.put(language, message);
            }
            messages.put(key, map);
        }
    }
}
