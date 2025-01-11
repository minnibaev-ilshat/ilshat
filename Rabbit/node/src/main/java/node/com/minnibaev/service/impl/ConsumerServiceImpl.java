package node.com.minnibaev.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.log4j.Log4j2;
import node.com.minnibaev.service.ConsumerService;
import node.com.minnibaev.service.MainService;
import node.com.minnibaev.service.ProducerService;

@Log4j2
@Service
public class ConsumerServiceImpl implements ConsumerService {

	private final MainService mainService;

	public ConsumerServiceImpl(MainService mainService) {
		this.mainService = mainService;
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.text-message-update}")
	public void consumeTextMessageUpdates(Update update) {
		System.out.println("NODE: text message from rabbitMQ received");
		mainService.processTextMessage(update);
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.doc-message-update}")
	public void consumeDocMessageUpdates(Update update) {
		System.out.println("NODE: doc message from rabbitMQ received");
		mainService.processDocMessage(update);
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.photo-message-update}")
	public void consumePhotoMessageUpdates(Update update) {
		System.out.println("NODE: photo message from rabbitMQ received");
		mainService.processPhotoMessage(update);

	}

}
