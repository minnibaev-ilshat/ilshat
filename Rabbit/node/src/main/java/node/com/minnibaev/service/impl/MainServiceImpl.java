package node.com.minnibaev.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import common.com.minnibaev.dao.AppUserDAO;
import common.com.minnibaev.entity.AppUser;
import common.com.minnibaev.entity.enums.UserState;
import node.com.minnibaev.dao.RawDataDAO;
import node.com.minnibaev.entity.RawData;
import node.com.minnibaev.service.MainService;
import node.com.minnibaev.service.ProducerService;

@Service
public class MainServiceImpl implements MainService {

	private final RawDataDAO rawDataDAO;
	private final ProducerService producerService;
	private final AppUserDAO appUserDAO;

	public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, AppUserDAO appUserDAO) {
		super();
		this.rawDataDAO = rawDataDAO;
		this.producerService = producerService;
		this.appUserDAO = appUserDAO;
	}

	@Override
	public void processTextMessage(Update update) {
		saveRawData(update);
		var telegramUser = update.getMessage().getFrom();
		var appUser = findOrSaveAppUser(telegramUser);

		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId());
		sendMessage.setText("message from NODE");
		System.out.println("NODE has sent message");
		producerService.producerAnswer(sendMessage);

	}

	public AppUser findOrSaveAppUser(User telegramUser) {
		AppUser persistentUser = appUserDAO.findByTelegramUserId(telegramUser.getId());
		if (persistentUser == null) {
			AppUser newUser = AppUser.builder().firstName(telegramUser.getFirstName())
					.lastName(telegramUser.getLastName()).username(telegramUser.getUserName()).isActive(true)
					.state(UserState.BASIC_STATE).build();
			return appUserDAO.save(newUser);
		}
		return persistentUser;
	}

	private void saveRawData(Update update) {
		RawData rawData = RawData.builder().event(update).build();
		rawDataDAO.save(rawData);
	}

}
