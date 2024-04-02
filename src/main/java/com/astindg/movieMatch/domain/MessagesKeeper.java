package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Language;
import org.springframework.core.env.Environment;

import java.util.*;

import static com.astindg.movieMatch.util.PropertyReader.getProperty;

public record MessagesKeeper(Environment env) {

    private static final Map<String, Map<Language, String>> messages = new HashMap<>();
    private static final String MESSAGES_PROPERTY_KEY = "user.message.";
    private static final List<String> keys = new ArrayList<>();

    static {
        keys.add("initial");
        keys.add("select_option");
        keys.add("friend.invite");
        keys.add("friend.invite.btn");
        keys.add("friend.invite.incorrect_code");
        keys.add("friend.invite.not_found");
        keys.add("friend.invite.already_in_list_error");
        keys.add("friend.select");
        keys.add("friend.selected");
        keys.add("friend.delete");
        keys.add("friend.delete.success");
        keys.add("friend.new");
        keys.add("friend.error.set_friend");
        keys.add("friend.error.delete_friend");
        keys.add("friend.error.empty_list");
        keys.add("movie");
        keys.add("movie.favorite_header");
        keys.add("movie.disliked_header");
        keys.add("movie.favorite");
        keys.add("movie.favorite.deleted_successfully");
        keys.add("movie.disliked.deleted_successfully");
        keys.add("movie.notify_new_match");
        keys.add("movie.error.null.list");
        keys.add("movie.error.empty.list");
        keys.add("movie.error.empty.favorite");
        keys.add("movie.error.empty.disliked");
        keys.add("movie.error.friend_not_selected");
        keys.add("movie.error.matches_friend_empty");
        keys.add("movie.error.favorite_not_found");
        keys.add("movie.error.disliked_not_found");
        keys.add("settings.select_language");
        keys.add("settings.select_language.error");
        keys.add("error.unknown_command");
        keys.add("error.unknown_callback");
        keys.add("error.try_again_or_contact_devs");
    }

    public String getMessage(String key, Language language) {
        return messages.get(key).get(language);
    }
    //initial method
    private void initializeMessages(){
        for (String key : keys) {
            String messageKey = MESSAGES_PROPERTY_KEY + key;

            Map<Language, String> map = new HashMap<>();

            for (Language language : Language.values()) {

                String propertyKey = messageKey +
                        '.' +
                        language.toString().toLowerCase();

                String message = getProperty(env, propertyKey);

                map.put(language, message);
            }
            messages.put(key, map);
        }
    }
}
