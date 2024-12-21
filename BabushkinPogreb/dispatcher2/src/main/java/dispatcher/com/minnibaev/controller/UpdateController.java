package dispatcher.com.minnibaev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.minnibaev.model.RabbitQueue.TEXT_MESSAGE_UPDATE;
import static com.minnibaev.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static com.minnibaev.model.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static com.minnibaev.model.RabbitQueue.ANSWER_MESSAGE;

import dispatcher.com.minnibaev.service.UpdateProducer;
import dispatcher.com.minnibaev.utils.MessageUtils;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class UpdateController {

	private TelegramBot bot;
	@Autowired
	private MessageUtils messageUtils;
	@Autowired
	private UpdateProducer updateProducer;

	public void registerBot(TelegramBot bot) {
		this.bot = bot;
	}

	public void processUpdate(Update update) {
		if (update == null) {
			log.error("Received update is null");
			return;
		}

		if (update.hasMessage()) {
			distributeMessageByType(update);
		} else {
			log.error("Received unsupported message type " + update);
		}
	}

	private void distributeMessageByType(Update update) {
		var message = update.getMessage();
		if (message.hasText()) {
			processTextMessage(update);
		} else if (message.hasPhoto()) {
			processPhotoMessage(update);
		} else if (message.hasDocument()) {
			processDocMessage(update);
		} else {
			setUnsupportedMessageTypeView(update);
		}

	}

	private void setFileIsReceivedView(Update update) {
		var messageToSend = messageUtils.generateSendMessageWithText(update, "File is uploaded, Handling...");
		setView(messageToSend);
	}

	private void processDocMessage(Update update) {
		updateProducer.produce(DOC_MESSAGE_UPDATE, update);
		setFileIsReceivedView(update);
	}

	private void processPhotoMessage(Update update) {
		updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
		setFileIsReceivedView(update);
	}

	private void processTextMessage(Update update) {
		updateProducer.produce(TEXT_MESSAGE_UPDATE, update);

	}

	private void setUnsupportedMessageTypeView(Update update) {
		var messageToSend = messageUtils.generateSendMessageWithText(update, "Unsupported message type");
		setView(messageToSend);

	}

	public void setView(SendMessage messageToSend) {
		bot.sendAnswerMessage(messageToSend);

	}
}
