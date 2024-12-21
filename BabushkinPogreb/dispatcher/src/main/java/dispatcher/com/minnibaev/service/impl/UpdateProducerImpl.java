package dispatcher.com.minnibaev.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import dispatcher.com.minnibaev.service.UpdateProducer;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class UpdateProducerImpl implements UpdateProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public void produce(String rabbitQueue, Update update) {
		log.debug(update.getMessage().getText());
		rabbitTemplate.convertAndSend(rabbitQueue, update);
	}

}
