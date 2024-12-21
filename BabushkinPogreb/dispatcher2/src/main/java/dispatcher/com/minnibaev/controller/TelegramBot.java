package dispatcher.com.minnibaev.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dispatcher.com.minnibaev.config.BotConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {

//	private static final Logger log = Logger.getLogger(TelegramBot.class);

	@Autowired
	BotConfig botConfig;
	@Autowired
	private UpdateController updateController;

	@Override
	public String getBotUsername() {

		return botConfig.getBotName();
	}

	@Override
	public String getBotToken() {
		return botConfig.getBotToken();
	}

	@PostConstruct
	public void init() {
		updateController.registerBot(this);
	}

	@Override
	public void onUpdateReceived(Update update) {
		updateController.processUpdate(update);
//		if (update.hasMessage() && update.getMessage().hasText()) {
//			long chatID = update.getMessage().getChatId();
//			message(String.valueOf(chatID), "hello");
//		}
	}

	private void message(String chatID, String text) {
		SendMessage message = new SendMessage();
		message.setChatId(chatID);
		message.setText(text);
		try {
			execute(message);
		} catch (TelegramApiException e) {
			log.error(e);
		}
	}

	public void sendAnswerMessage(SendMessage message) {
		if (message != null) {
			try {
				execute(message);
			} catch (TelegramApiException e) {
				log.error(e);
			}
		} else
			log.debug("SendMessage is null");
	}

}
