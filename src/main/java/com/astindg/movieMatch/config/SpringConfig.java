package com.astindg.movieMatch.config;

import com.astindg.movieMatch.domain.CommandHandler;
import com.astindg.movieMatch.telegram.TelegramBot;
import com.astindg.movieMatch.telegram.TelegramNotifier;
import com.astindg.movieMatch.telegram.TemporaryKeyKeeper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Configuration
@ComponentScan("com.astindg.movieMatch")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.astindg.movieMatch.repositories")
@EntityScan("com.astindg.movieMatch.model")
public class SpringConfig {
    @Bean
    public TelegramBotsApi getTelegramApi() {
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return telegramBotsApi;
    }

    @Bean
    public TelegramBot getTelegramBot(TelegramBotsApi api, CommandHandler commandHandler){
        TelegramBot bot = new TelegramBot(TemporaryKeyKeeper.BOT_TOKEN, TemporaryKeyKeeper.BOT_NAME, commandHandler);
        try {
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return bot;
    }

    @Bean
    public TelegramNotifier startThreadTelegramNotifier(TelegramBot bot, CommandHandler handler){
        TelegramNotifier telegramNotifier = new TelegramNotifier(bot, handler);
        telegramNotifier.setName("Telegram Notifier");
        telegramNotifier.start();
        return telegramNotifier;
    }

}
