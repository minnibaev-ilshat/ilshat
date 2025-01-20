package node.com.minnibaev.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.RequiredArgsConstructor;
import node.com.minnibaev.service.ProducerService;

@RequiredArgsConstructor
@Service
public class ProducerServiceImpl implements ProducerService {

	private final RabbitTemplate rabbitTemplate;


	@Override
	public void producerAnswer(String answerMessageQueue, SendMessage sendMessage) {
		rabbitTemplate.convertAndSend(answerMessageQueue, sendMessage);

	}

}
