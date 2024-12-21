package node.com.minnibaev.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.log4j.Log4j;
import node.com.minnibaev.service.ConsumerService;
import node.com.minnibaev.service.MainService;

import static com.minnibaev.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService{
	
	@Autowired
	private MainService mainService;


	@Override
	@RabbitListener(queues = TEXT_MESSAGE_UPDATE)
	public void consumeTextMessageUpdates(Update update) {
		log.debug("NODE: Text message is received");
		mainService.processTextMessage(update);
	}

	@Override
	@RabbitListener(queues = DOC_MESSAGE_UPDATE)
	public void consumeDocMessageUpdates(Update update) {
		log.debug("NODE: Doc message is received");
	}

	@Override
	@RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
	public void consumePhotoMessageUpdates(Update update) {
		log.debug("NODE: Photo message is received");
	}

}
