package com.astindg.movieMatch.model;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Message {
    private String text;
    private List<Map<String, String>> buttons;
    private List<List<String>> keyboard;
    private File image;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
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
}
