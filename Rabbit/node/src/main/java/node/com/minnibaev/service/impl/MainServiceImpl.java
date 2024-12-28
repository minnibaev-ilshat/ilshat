package node.com.minnibaev.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import common.com.minnibaev.entity.AppUser;
import common.com.minnibaev.entity.enums.UserState;
import lombok.extern.log4j.Log4j2;
import common.com.minnibaev.dao.AppUserDAO;
import node.com.minnibaev.dao.RawDataDAO;
import node.com.minnibaev.entity.RawData;
import node.com.minnibaev.service.MainService;
import node.com.minnibaev.service.ProducerService;
import node.com.minnibaev.service.enums.ServiceCommands;

@Log4j2
@Service
public class MainServiceImpl implements MainService {

	private final RawDataDAO rawDataDAO;
	private final ProducerService producerService;
	private final AppUserDAO appUserDAO;

	@Autowired
	public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, AppUserDAO appUserDAO) {
		this.rawDataDAO = rawDataDAO;
		this.producerService = producerService;
		this.appUserDAO = appUserDAO;
	}

	@Override
	public void processTextMessage(Update update) {
		saveRawData(update);
		var appUser = findOrSaveAppUser(update);
		var userState = appUser.getState();
		var receivedMessage = update.getMessage().getText();
		var output = "";

		if (ServiceCommands.CANCEL.equals(receivedMessage)) {
			output = cancelProcess(appUser);
		} else if (UserState.BASIC_STATE.equals(userState)) {
			output = processServiceCommand(appUser, receivedMessage);
		} else if (UserState.WAIT_FOR_EMAIL_STATE.equals(userState)) {
			// TODO add email

		} else {
			output = "Unknown command. Please, press /cancel";
			log.error("Unknown user state: " + userState);
		}

		sendAnswer(output, update.getMessage().getChatId());
	}

	private void sendAnswer(String output, Long chatId) {

		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText("message from NODE");
		System.out.println("NODE has sent message");
		producerService.producerAnswer(sendMessage);

	}

	private String processServiceCommand(AppUser appUser, String cmd) {
		if (ServiceCommands.REGISTRATION.equals(cmd)) {
			// TODO add registration
		}
		if (ServiceCommands.START.equals(cmd)) {
			return "Hello, to see all command press /help";
		}
		if (ServiceCommands.HELP.equals(cmd)) {
			return help();
		} else
			return "Unknown command. Please, press /cancel";
	}

	private String help() {
		String cmds = "/help \n /start \n /registration";
		return cmds;
	}

	private String cancelProcess(AppUser appUser) {
		appUser.setState(UserState.BASIC_STATE);
		appUserDAO.save(appUser);
		return "Command is cancelled";
	}

	public AppUser findOrSaveAppUser(Update update) {
		User telegramUser = update.getMessage().getFrom();
		AppUser persistentUser = appUserDAO.findByTelegramUserId(telegramUser.getId());
		if (persistentUser == null) {
			AppUser newUser = AppUser.builder().firstName(telegramUser.getFirstName())
					.lastName(telegramUser.getLastName()).username(telegramUser.getUserName())
					.telegramUserId(telegramUser.getId()).isActive(true).state(UserState.BASIC_STATE).build();
			return appUserDAO.save(newUser);
		}
		return persistentUser;
	}

	private void saveRawData(Update update) {
		RawData rawData = RawData.builder().event(update).build();
		rawDataDAO.save(rawData);
	}

	@Override
	public void processPhotoMessage(Update update) {
		saveRawData(update);
		var appUser = findOrSaveAppUser(update);
		var chatID = update.getMessage().getChatId();
		if (isNotAllowToSendContent(chatID, appUser)) {
			return;
		}
		// TODO add uploading photo
		sendAnswer("The photo uploaded", chatID);
	}

	@Override
	public void processDocMessage(Update update) {
		saveRawData(update);
		var appUser = findOrSaveAppUser(update);
		var chatID = update.getMessage().getChatId();
		if (isNotAllowToSendContent(chatID, appUser)) {
			return;
		}
		// TODO add uploading photo
		sendAnswer("The document uploaded", chatID);
	}

	private boolean isNotAllowToSendContent(Long chatID, AppUser appUser) {
		var userState = appUser.getState();
		if (!appUser.getIsActive()) {
			var error = "Log in or sign up";
			sendAnswer(error, chatID);
			return true;
		} else if (!UserState.BASIC_STATE.equals(userState)) {
			var error = "For uploading file, cancel this command pressing /cancel";
			sendAnswer(error, chatID);
			return true;
		}
		return false;
	}

}
