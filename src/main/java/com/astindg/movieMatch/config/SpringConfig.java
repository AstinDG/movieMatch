package com.astindg.movieMatch.config;

import com.astindg.movieMatch.domain.CommandHandler;
import com.astindg.movieMatch.telegram.CommandTranslator;
import com.astindg.movieMatch.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static com.astindg.movieMatch.util.PropertyReader.getProperty;

@Configuration
@ComponentScan("com.astindg.movieMatch")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.astindg.movieMatch.repositories")
@EntityScan("com.astindg.movieMatch.model")
public class SpringConfig{
    private static final String BOT_TOKEN_PROPERTY_KEY = "movie_match.telegram_bot.token";
    private static final String BOT_NAME_PROPERTY_KEY = "movie_match.telegram_bot.name";

    @Autowired
    Environment env;

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
    public TelegramBot getTelegramBot(TelegramBotsApi api, CommandHandler commandHandler, CommandTranslator translator){
        String botName = getProperty(this.env, BOT_NAME_PROPERTY_KEY);
        String botToken = getProperty(this.env, BOT_TOKEN_PROPERTY_KEY);

        TelegramBot bot = new TelegramBot(botToken, botName, commandHandler, translator);
        try {
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return bot;
    }

}
