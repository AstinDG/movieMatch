package com.astindg.movieMatch.telegram;

import com.astindg.movieMatch.domain.CommandHandler;
import com.astindg.movieMatch.model.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class TelegramNotifier extends Thread {
    private final TelegramBot telegramBot;
    private final CommandHandler commandHandler;
    private Long lastChatId = 0L;

    private boolean isRunning = true;
    private static final int TIME_SLEEP_THREAD_IN_SECONDS = 3;

    @Autowired
    public TelegramNotifier(TelegramBot telegramBot, CommandHandler commandHandler) {
        this.telegramBot = telegramBot;
        this.commandHandler = commandHandler;
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(TIME_SLEEP_THREAD_IN_SECONDS);
        } catch (InterruptedException exception) {
            this.isRunning = false;
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            if (!commandHandler.getNotifyQueue().isEmpty()) {
                Pair<Long, Message> message = commandHandler.getNotifyQueue().poll();
                assert message != null;
                if(message.getLeft().equals(lastChatId)){
                    sleep();
                }
                this.telegramBot.sendMessage(message.getLeft(), message.getRight());
                this.lastChatId = message.getLeft();
            }
        }
    }
}
