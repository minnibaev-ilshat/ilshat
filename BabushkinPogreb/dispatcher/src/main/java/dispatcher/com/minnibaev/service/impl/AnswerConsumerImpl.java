package dispatcher.com.minnibaev.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import dispatcher.com.minnibaev.controller.UpdateController;
import dispatcher.com.minnibaev.service.AnswerConsumer;

import static com.minnibaev.model.RabbitQueue.ANSWER_MESSAGE;

@Service
public class AnswerConsumerImpl implements AnswerConsumer{

	@Autowired
	private UpdateController updateController;

	@Override
	@RabbitListener(queues = ANSWER_MESSAGE)
	public void consume(SendMessage sendMessage) {
		updateController.setView(sendMessage);
	}

}
