package com.astindg.movieMatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class MovieMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieMatchApplication.class, args);
		/*TelegramNotifier telegramNotifier = new TelegramNotifier();
		Thread matchNotifierThread = new Thread(telegramNotifier);
		matchNotifierThread.start();

		CommandHandler commandHandler = new CommandHandlerImpl(messageBuilder, telegramNotifier);
		try {
			TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
			TelegramBot telegramBot = new TelegramBot(commandHandler);
			telegramNotifier.setTelegramBot(telegramBot);
			api.registerBot(telegramBot);

		} catch (TelegramApiException e) {
			e.printStackTrace();
		}*/

	}

}
