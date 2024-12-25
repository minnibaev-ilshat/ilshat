package node.com.minnibaev.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import node.com.minnibaev.dao.RawDataDAO;
import node.com.minnibaev.entity.RawData;
import node.com.minnibaev.service.MainService;
import node.com.minnibaev.service.ProducerService;

@Service
public class MainServiceImpl implements MainService {

	private final RawDataDAO rawDataDAO;
	private final ProducerService producerService;

	public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService) {
		super();
		this.rawDataDAO = rawDataDAO;
		this.producerService = producerService;
	}

	@Override
	public void processTextMessage(Update update) {
		saveRawData(update);

		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId());
		sendMessage.setText("message from NODE");
		producerService.producerAnswer(sendMessage);

	}

	private void saveRawData(Update update) {
		RawData rawData = RawData.builder().event(update).build();
		rawDataDAO.save(rawData);
	}

}
