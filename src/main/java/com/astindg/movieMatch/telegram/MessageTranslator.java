package com.astindg.movieMatch.telegram;

import com.astindg.movieMatch.model.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageTranslator {
    private static final String PARSE_MODE = "html";

    protected static SendMessage getSendMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(PARSE_MODE);
        if(message.hasText()){
            sendMessage.setText(message.getText());
            if (message.hasButtons()) {
                InlineKeyboardMarkup buttons = convertButtons(message.getButtons());
                sendMessage.setReplyMarkup(buttons);
            }
            if (message.hasKeyboard()) {
                ReplyKeyboardMarkup markup = convertKeyboard(message.getKeyboard());
                sendMessage.setReplyMarkup(markup);
            }
        }

        return sendMessage;
    }

    protected static SendPhoto getSendPhoto(Message message){
        SendPhoto sendPhoto = new SendPhoto();
        InputFile image = new InputFile().setMedia(message.getImage());
        sendPhoto.setPhoto(image);
        sendPhoto.setCaption(message.getText());
        sendPhoto.setParseMode(PARSE_MODE);

        if (message.hasButtons()) {
            InlineKeyboardMarkup buttons = convertButtons(message.getButtons());
            sendPhoto.setReplyMarkup(buttons);
        }
        if (message.hasKeyboard()) {
            ReplyKeyboardMarkup markup = convertKeyboard(message.getKeyboard());
            sendPhoto.setReplyMarkup(markup);
        }

        return sendPhoto;
    }

    protected static EditMessageReplyMarkup getEditMessageMarkup(Message message){
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        InlineKeyboardMarkup buttons = convertButtons(message.getButtons());
        editMarkup.setReplyMarkup(buttons);
        editMarkup.setMessageId(message.getEditMessageId());
        return editMarkup;
    }

    private static InlineKeyboardMarkup convertButtons(List<Map<String, String>> buttons){
        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();

        for(Map<String, String> button : buttons){
            List<InlineKeyboardButton> row = new ArrayList<>();
            for(String buttonText : button.keySet()){
                InlineKeyboardButton btn = new InlineKeyboardButton();
                btn.setText(buttonText);
                btn.setCallbackData(button.get(buttonText));
                row.add(btn);
            }
            keyboardButtons.add(row);
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboardButtons);
        return markup;
    }

    private static ReplyKeyboardMarkup convertKeyboard(List<List<String>> keyboard) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setIsPersistent(true);
        markup.setSelective(true);
        markup.setOneTimeKeyboard(false);
        markup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        for (List<String> row : keyboard) {
            List<KeyboardButton> keyboardRow = new ArrayList<>();

            for (String button : row) {
                keyboardRow.add(new KeyboardButton(button));
            }
            keyboardRows.add(new KeyboardRow(keyboardRow));
        }

        markup.setKeyboard(keyboardRows);
        return markup;
    }
}
