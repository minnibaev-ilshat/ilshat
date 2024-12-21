package node.com.minnibaev.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import common.com.minnibaev.dao.AppUserRep;
import common.com.minnibaev.entity.AppUser;
import common.com.minnibaev.entity.enums.UserState;
import lombok.extern.log4j.Log4j;
import node.com.minnibaev.dao.RawDataDAO;
import node.com.minnibaev.entity.RawData;
import node.com.minnibaev.service.MainService;
import node.com.minnibaev.service.ProducerService;

@Service
@Log4j
public class MainServiceImpl implements MainService {

	private final RawDataDAO rawDataDAO;
	private final ProducerService producerService;
	private final AppUserRep appUserDAO;

	public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, AppUserRep appUserDAO) {
		this.rawDataDAO = rawDataDAO;
		this.producerService = producerService;
		this.appUserDAO = appUserDAO;
	}
//	public MainServiceImpl(AppUserDAO appUserDAO) {
//		this.appUserDAO = appUserDAO;
//	}

	@Override
	public void processTextMessage(Update update) {
		saveRawData(update);
		var receivedMessage = update.getMessage();
		var telegramUser = receivedMessage.getFrom();
		var appUser = findOrSaveAppUser(telegramUser);

		var sendMessage = new SendMessage();
		sendMessage.setChatId(receivedMessage.getChatId());
		sendMessage.setText("Hello from NODE");
		producerService.produceAnswer(sendMessage);
	}

	private AppUser findOrSaveAppUser(User telegramUser) {
//		AppUser persistentAppUser = appUserDAO.findAppUserByTelegramId(telegramUser.getId());
//		if (persistentAppUser == null) {
//			AppUser newUser = AppUser.builder().firstName(telegramUser.getFirstName())
//					.lastName(telegramUser.getLastName()).username(telegramUser.getUserName())
//					// TODO need to include dependency
//					.isActive(true).state(UserState.BASIC_STATE).build();
//			return appUserDAO.save(newUser);
//		}
//		return appUserDAO.save(persistentAppUser);
		return null;
	}

	private void saveRawData(Update update) {
		RawData rawData = RawData.builder().event(update).build();
		rawDataDAO.save(rawData);
	}

}
