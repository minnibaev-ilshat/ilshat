package node.com.minnibaev.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import node.com.minnibaev.service.ConsumerService;
import node.com.minnibaev.service.MainService;
import node.com.minnibaev.service.ProducerService;

@Service
public class ConsumerServiceImpl implements ConsumerService {

	private final ProducerService producerService;

	private final MainService mainService;

	public ConsumerServiceImpl(ProducerService producerService, MainService mainService) {
		this.producerService = producerService;
		this.mainService = mainService;
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.text-message-update}")
	public void consumeTextMessageUpdates(Update update) {
		mainService.processTextMessage(update);
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.doc-message-update}")
	public void consumeDocMessageUpdates(Update update) {
		mainService.processDocMessage(update);
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.photo-message-update}")
	public void consumePhotoMessageUpdates(Update update) {
		mainService.processPhotoMessage(update);

	}

}
