package dispatcher.com.minnibaev.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import dispatcher.com.minnibaev.controller.UpdateProcessor;
import dispatcher.com.minnibaev.service.AnswerConsumer;

@RequiredArgsConstructor
@Service
public class AnswerConsumerImpl implements AnswerConsumer {

	private final UpdateProcessor updateProcessor;

	@RabbitListener(queues = "${spring.rabbitmq.queues.answer-message}")
	public void consume(SendMessage sendMessage) {
		updateProcessor.setView(sendMessage);
	}
}