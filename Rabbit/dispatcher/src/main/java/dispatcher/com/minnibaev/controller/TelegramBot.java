package dispatcher.com.minnibaev.controller;

import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import jakarta.annotation.PostConstruct;

//@Log4j
//@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {

	@Value("${bot.name}")
	private String botName;

	@Value("${bot.token}")
	private String botToken;

	private UpdateProcessor updateProcessor;

	@PostConstruct
	public void init() {
		updateProcessor.registerBot(this);
	}

	@Override
	public String getBotUsername() {
		return botName;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	public void sendAnswerMessage(SendMessage message) {
		if (message != null) {
			try {
				execute(message);
			} catch (TelegramApiException e) {

			}
		}
	}

	@Override
	public void onUpdateReceived(Update update) {
		updateProcessor.processUpdate(update);
	}

	public TelegramBot(UpdateProcessor updateProcessor) {
		this.updateProcessor = updateProcessor;
	}
}