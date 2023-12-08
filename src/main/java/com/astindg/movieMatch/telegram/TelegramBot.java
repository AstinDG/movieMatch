package com.astindg.movieMatch.telegram;

import com.astindg.movieMatch.domain.Command;
import com.astindg.movieMatch.domain.CommandHandler;
import com.astindg.movieMatch.model.Message;
import com.astindg.movieMatch.model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

import static com.astindg.movieMatch.telegram.CommandTranslator.translateCommand;
import static com.astindg.movieMatch.telegram.MessageTranslator.*;

public class TelegramBot extends TelegramLongPollingBot {

    private final CommandHandler commandHandler;
    private final String botName;

    public TelegramBot(String botToken, String botName, CommandHandler commandHandler) {
        super(botToken);
        this.botName = botName;
        this.commandHandler = commandHandler;
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
        String callBackQuery = update.getCallbackQuery().getData();

        Message message = commandHandler.getReplyCallbackQuery(user, callBackQuery);

        sendMessage(chatId, message);
    }

    private void sendReply(Update update) {
        Optional<Command> command = translateCommand(update.getMessage().getText());

        String name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
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
        if (message.hasImage()) {
            sendImage(chatId, message);
        } else {
            sendMessage(chatId, message);
        }
    }

    public void sendMessage(Long chatId, Message message) {
        SendMessage sendMessage = getSendMessage(message);
        sendMessage.setChatId(chatId);
        sendApiMethodAsync(sendMessage);
    }

    public void sendImage(Long chatId, Message message) {
        SendPhoto sendPhoto = getSendPhoto(message);
        sendPhoto.setChatId(chatId);
        executeAsync(sendPhoto);
    }
}