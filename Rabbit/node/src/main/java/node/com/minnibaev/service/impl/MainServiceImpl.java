package node.com.minnibaev.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import common.com.minnibaev.entity.AppUser;
import common.com.minnibaev.entity.enums.UserState;
import common.com.minnibaev.dao.AppUserDAO;
import node.com.minnibaev.dao.RawDataDAO;
import node.com.minnibaev.entity.RawData;
import node.com.minnibaev.service.MainService;
import node.com.minnibaev.service.ProducerService;

//@EnableJpaRepositories()
//@EntityScan(basePackages = "Rabbit")
@Service
public class MainServiceImpl implements MainService {

	private final RawDataDAO rawDataDAO;
	private final ProducerService producerService;
	private final AppUserDAO appUserDAONode;

	@Autowired
	public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, AppUserDAO appUserDAONode) {
		this.rawDataDAO = rawDataDAO;
		this.producerService = producerService;
		this.appUserDAONode = appUserDAONode;
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
		AppUser persistentUser = appUserDAONode.findByTelegramUserId(telegramUser.getId());
		if (persistentUser == null) {
			AppUser newUser = AppUser.builder().firstName(telegramUser.getFirstName())
					.lastName(telegramUser.getLastName()).username(telegramUser.getUserName())
					.telegramUserId(telegramUser.getId()).isActive(true).state(UserState.BASIC_STATE).build();
			return appUserDAONode.save(newUser);
		}
		return persistentUser;
	}

	private void saveRawData(Update update) {
		RawData rawData = RawData.builder().event(update).build();
		rawDataDAO.save(rawData);
	}

}
