package node.com.minnibaev.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ProducerService {
	void producerAnswer(String answerMessageQueue, SendMessage sendMessage);
}
