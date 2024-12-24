package node.com.minnibaev.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import node.com.minnibaev.service.ProducerService;

@Service
public class ProducerServiceImpl implements ProducerService {

	private final RabbitTemplate rabbitTemplate;
	
	
	
	public ProducerServiceImpl(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}



	@Override
	public void producerAnswer(SendMessage sendMessage) {
		rabbitTemplate.convertAndSend("${spring.rabbitmq.queues.answer-message}",sendMessage);
		
	}

}
