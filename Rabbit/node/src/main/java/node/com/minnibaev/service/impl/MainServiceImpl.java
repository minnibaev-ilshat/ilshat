package node.com.minnibaev.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import common.com.minnibaev.entity.AppDocument;
import common.com.minnibaev.entity.AppPhoto;
import common.com.minnibaev.entity.AppUser;
import common.com.minnibaev.entity.enums.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import common.com.minnibaev.dao.AppUserDAO;
import node.com.minnibaev.configuration.RabbitConfiguration;
import node.com.minnibaev.dao.RawDataDAO;
import node.com.minnibaev.entity.RawData;
import node.com.minnibaev.exceptions.UploadFileException;
import node.com.minnibaev.service.AppUserService;
import node.com.minnibaev.service.FileService;
import node.com.minnibaev.service.MainService;
import node.com.minnibaev.service.ProducerService;
import node.com.minnibaev.service.enumsnode.LinkType;
import node.com.minnibaev.service.enumsnode.ServiceCommands;

//@Log4j2
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {

	private final RawDataDAO rawDataDAO;
	private final ProducerService producerService;
	private final AppUserDAO appUserDAO;
	private final FileService fileService;
	private final AppUserService appUserService;
	private final RabbitConfiguration rabbitConfiguration;

	@Override
	public void processTextMessage(Update update) {
		saveRawData(update);
		var appUser = findOrSaveAppUser(update);
		var userState = appUser.getState();
		var receivedMessage = update.getMessage().getText();
		var output = "";
		var serviceCommand = ServiceCommands.fromValue(receivedMessage);
		if (ServiceCommands.CANCEL.equals(serviceCommand)) {
			output = cancelProcess(appUser);
		} else if (UserState.BASIC_STATE.equals(userState)) {
			output = processServiceCommand(appUser, serviceCommand);
		} else if (UserState.WAIT_FOR_EMAIL_STATE.equals(userState)) {
			output = appUserService.setEmail(appUser, receivedMessage);

		} else {
			output = "Unknown command. Please, press /cancel";
//			log.error("Unknown user state: " + userState);
		}

		sendAnswer(output, update.getMessage().getChatId());
	}

	private void sendAnswer(String output, Long chatId) {

		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(output);
		System.out.println("NODE has sent message to telegram");
		producerService.producerAnswer(rabbitConfiguration.getAnswerMessageQueue(), sendMessage);

	}

	private String processServiceCommand(AppUser appUser, ServiceCommands cmd) {
		if (ServiceCommands.REGISTRATION == cmd) {
			return appUserService.registerUser(appUser);
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
		String cmds = "/help \n/start \n/registration";
		return cmds;
	}

	private String cancelProcess(AppUser appUser) {
		appUser.setState(UserState.BASIC_STATE);
		appUserDAO.save(appUser);
		return "Command is cancelled";
	}

	public AppUser findOrSaveAppUser(Update update) {
		User telegramUser = update.getMessage().getFrom();
		var optional = appUserDAO.findByTelegramUserId(telegramUser.getId());
		if (optional.isEmpty()) {
			AppUser newUser = AppUser.builder().firstName(telegramUser.getFirstName())
					.lastName(telegramUser.getLastName()).username(telegramUser.getUserName())
					.telegramUserId(telegramUser.getId()).isActive(false).state(UserState.BASIC_STATE).build();
			return appUserDAO.save(newUser);
		}
		return optional.get();
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
		try {
			AppPhoto photo = fileService.proccessPhoto(update.getMessage());
			String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO);
			var answer = "Photo is uploaded, click the link, please \n" + link;
			sendAnswer(answer, chatID);
		} catch (UploadFileException e) {
			System.out.println("NODE: Uploading is failed");
//			log.error(e);
			var error = "Uploading failed, please, try again";
			sendAnswer(error, chatID);
		}
	}

	@Override
	public void processDocMessage(Update update) {
		saveRawData(update);
		var appUser = findOrSaveAppUser(update);
		var chatID = update.getMessage().getChatId();
		if (isNotAllowToSendContent(chatID, appUser)) {
			System.out.println("NODE: User is not allowed to send message");
			return;
		}
		try {
			AppDocument doc = fileService.proccessDoc(update.getMessage());
			String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC);
			var answer = "Doc is uploaded, click the link, please \n" + link;
			sendAnswer(answer, chatID);
		} catch (UploadFileException e) {
			System.out.println("NODE: Uploading is failed");
//			log.error(e);
			var error = "Uploading failed, please, try again";
			sendAnswer(error, chatID);
		}
	}

	private boolean isNotAllowToSendContent(Long chatID, AppUser appUser) {
		var userState = appUser.getState();
		if (!appUser.getIsActive()) {
			var error = "Please, register your email adress, press /registration";
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
