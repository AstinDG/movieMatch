package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import org.springframework.core.env.Environment;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.nio.charset.StandardCharsets.*;

public class MessageKeeper {

    private final Map<String, Map<Language, String>> messages;
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

    public MessageKeeper(Environment env) {
        this.messages = new HashMap<>();

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
            this.messages.put(key, map);
        }

    }

    public String getMessage(String key, Language language) {
        return messages.get(key).get(language);
    }
}
