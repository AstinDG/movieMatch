package com.astindg.movieMatch.domain;

import com.astindg.movieMatch.model.Command;
import com.astindg.movieMatch.model.Message;
import com.astindg.movieMatch.model.User;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Queue;

public interface CommandHandler {

    Message getReply(User user, Command command);

    Message getReply(User user, String message);

    Message getReplyCallbackQuery(User user, CallbackQuery callbackQuery);

    Queue<Pair<Long, Message>> getNotifyQueue();
}
