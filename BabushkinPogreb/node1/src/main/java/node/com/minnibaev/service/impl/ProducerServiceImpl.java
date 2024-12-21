package node.com.minnibaev.service.impl;

import static com.minnibaev.model.RabbitQueue.ANSWER_MESSAGE;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.extern.log4j.Log4j;
import node.com.minnibaev.service.ProducerService;

@Service
@Log4j
public class ProducerServiceImpl implements ProducerService {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public void produceAnswer(SendMessage message) {
		rabbitTemplate.convertAndSend(ANSWER_MESSAGE, message);
	}

}
