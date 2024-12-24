package node.com.minnibaev.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import node.com.minnibaev.service.ConsumerService;
import node.com.minnibaev.service.ProducerService;

@Service
public class ConsumerServiceImpl implements ConsumerService {

	private final ProducerService producerService;

	public ConsumerServiceImpl(ProducerService producerService) {
		this.producerService = producerService;
	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.text-message-update}")
	public void consumeTextMessageUpdates(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId());
		sendMessage.setText("message from NODE");
		producerService.producerAnswer(sendMessage);

	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.doc-message-update}")
	public void consumeDocMessageUpdates(Update update) {
		// TODO Auto-generated method stub

	}

	@Override
	@RabbitListener(queues = "${spring.rabbitmq.queues.photo-message-update}")
	public void consumePhotoMessageUpdates(Update update) {
		// TODO Auto-generated method stub

	}

}
