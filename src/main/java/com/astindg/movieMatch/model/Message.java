package com.astindg.movieMatch.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Message {
    private String text;
    private List<List<Pair<String, String>>> buttons;
    private List<List<String>> keyboard;
    private File image;
    private Integer editMessageId;
    private boolean hasEditButtons;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public Message(String text, List<List<Pair<String, String>>> buttons, List<List<String>> keyboard, File image) {
        this.text = text;
        this.buttons = buttons;
        this.keyboard = keyboard;
        this.image = image;
    }

    public boolean hasButtons() {
        return (buttons != null && !buttons.isEmpty());
    }

    public boolean hasKeyboard() {
        return (keyboard != null && !keyboard.isEmpty());
    }

    public boolean hasText() {
        return (text != null && !text.isEmpty());
    }

    public boolean hasImage() {
        return image != null;
    }

    public boolean hasEditButtons() {
        return hasEditButtons;
    }
}
