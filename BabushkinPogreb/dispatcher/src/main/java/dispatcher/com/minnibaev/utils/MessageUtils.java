package dispatcher.com.minnibaev.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUtils {
	public SendMessage generateSendMessageWithText(Update update, String text) {
		var messageToSend = new SendMessage();
		messageToSend.setChatId(update.getMessage().getChatId());
		messageToSend.setText(text);
		return messageToSend;

	}
}
