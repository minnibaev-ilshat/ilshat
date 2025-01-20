package node.com.minnibaev.service.impl;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import common.com.minnibaev.dao.AppUserDAO;
import common.com.minnibaev.dto.MailParams;
import common.com.minnibaev.entity.AppUser;
import common.com.minnibaev.entity.enums.UserState;
import common.com.minnibaev.utils.CryptoTool;
import lombok.RequiredArgsConstructor;
import node.com.minnibaev.configuration.RabbitConfiguration;
import node.com.minnibaev.service.AppUserService;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

	private final AppUserDAO appUserDAO;

	private final CryptoTool cryptoTool;

	private final RabbitTemplate rabbitTemplate;
	
	private final RabbitConfiguration rabbitConfiguration;

	@Override
	public String registerUser(AppUser appUser) {
		if (appUser.getIsActive())
			return "User is already registered";
		else if (appUser.getEmail() != null)
			return "Mail is already sent. Check your mailbox, please";
		appUser.setState(UserState.WAIT_FOR_EMAIL_STATE);
		appUserDAO.save(appUser);
		return "Put in your email address, please";
	}

	@Override
	public String setEmail(AppUser appUser, String email) {
		try {
			InternetAddress emailAddress = new InternetAddress(email);
			emailAddress.validate();
		} catch (AddressException e) {
			return "Please, put in correct email. Or press /cancel";
		}
		var optional = appUserDAO.findByEmail(email);
		if (optional.isEmpty()) {
			appUser.setEmail(email);
			appUser.setState(UserState.BASIC_STATE);
			appUser = appUserDAO.save(appUser);

			var cryptoUserId = cryptoTool.hashOf(appUser.getId());
			sendRegistrationMail(cryptoUserId, email);
			return "Email is sent, check your mailbox, please";
		}
		return "Email is using by other user. Put in another email adress, please";

	}

	private void sendRegistrationMail(String cryptoUserId, String email) {
		MailParams mailParams = MailParams.builder().emailTo(email).id(cryptoUserId).build();
		rabbitTemplate.convertAndSend(rabbitConfiguration.getRegistrationMailQueue(), mailParams);
	}

}
