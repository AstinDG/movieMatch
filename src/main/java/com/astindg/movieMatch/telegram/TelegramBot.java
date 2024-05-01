package com.astindg.movieMatch.telegram;

import com.astindg.movieMatch.model.Command;
import com.astindg.movieMatch.domain.CommandHandler;
import com.astindg.movieMatch.model.Message;
import com.astindg.movieMatch.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static com.astindg.movieMatch.telegram.MessageTranslator.*;

public class TelegramBot extends TelegramLongPollingBot {

    private final CommandHandler commandHandler;
    private final String botName;
    private final CommandTranslator translator;

    @Autowired
    public TelegramBot(String botToken, String botName, CommandHandler commandHandler, CommandTranslator translator) {
        super(botToken);
        this.botName = botName;
        this.commandHandler = commandHandler;
        this.translator = translator;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            if (update.getMessage().hasText() || update.hasCallbackQuery()) {
                sendReply(update);
            }
        } else if (update.hasCallbackQuery()) {
            sendReplyCallBackQuery(update);
        }
    }

    private void sendReplyCallBackQuery(Update update) {
        String name = update.getCallbackQuery().getFrom().getFirstName() + " " + update.getCallbackQuery().getFrom().getLastName();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        User user = new User();
        user.setName(name);
        user.setChatId(chatId);
        CallbackQuery callBackQuery = update.getCallbackQuery();

        Message message = commandHandler.getReplyCallbackQuery(user, callBackQuery);

        sendMessage(chatId, message);
    }

    private void sendReply(Update update) {
        Optional<Command> command = translator.translateCommand(update.getMessage().getText());
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        //name format FirstName_LastName if last name is present
        String name = (lastName != null) ? firstName + " " + lastName : firstName;

        Long chatId = update.getMessage().getChatId();
        User user = new User();
        user.setName(name);
        user.setChatId(chatId);
        Message message;
        if (command.isEmpty()) {
            message = commandHandler.getReply(user, update.getMessage().getText());
        } else {
            message = commandHandler.getReply(user, command.get());
        }
        sendMessage(chatId, message);

    }

    public void sendMessage(Long chatId, Message message) {
        if (message.hasImage()) {
            sendImage(chatId, message);
        } else if(message.hasEditButtons()){
            sendEditButtonsMessage(chatId, message);
        } else {
            SendMessage sendMessage = getSendMessage(message);
            sendMessage.setChatId(chatId);
            sendApiMethodAsync(sendMessage);
        }
    }

    public void sendImage(Long chatId, Message message) {
        SendPhoto sendPhoto = getSendPhoto(message);
        sendPhoto.setChatId(chatId);
        executeAsync(sendPhoto);
    }

    public void sendEditButtonsMessage(Long chatId, Message message){
        EditMessageReplyMarkup editMessage = getEditMessageMarkup(message);
        editMessage.setChatId(chatId);
        try {
            executeAsync(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}