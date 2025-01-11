package node.com.minnibaev.service.impl;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import common.com.minnibaev.dao.AppUserDAO;
import common.com.minnibaev.dto.MailParams;
import common.com.minnibaev.entity.AppUser;
import common.com.minnibaev.entity.enums.UserState;
import common.com.minnibaev.utils.CryptoTool;
import node.com.minnibaev.service.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService {

	private final AppUserDAO appUserDAO;

	private final CryptoTool cryptoTool;

	@Value("${service.mail.uri}")
	private String mailServiceUri;

	public AppUserServiceImpl(AppUserDAO appUserDAO, CryptoTool cryptoTool) {
		super();
		this.appUserDAO = appUserDAO;
		this.cryptoTool = cryptoTool;
	}

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
			var response = sendRequestToMailService(cryptoUserId, email);
			if (response.getStatusCode() != HttpStatus.OK) {
				var message = String.format("Some problems with sending email", email);
				System.out.println(message);
				appUser.setEmail(null);
				appUserDAO.save(appUser);
				return message;
			}
			return "Email is sent, check your mailbox, please";
		}
		return "Email is using by other user. Put in another email adress, please";
	}

	private ResponseEntity<?> sendRequestToMailService(String cryptoUserId, String email) {
		var restTemplate = new RestTemplate();
		var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		var mailParams = MailParams.builder().id(cryptoUserId).emailTo(email).build();
		var request = new HttpEntity<MailParams>(mailParams, headers);

		return restTemplate.exchange(mailServiceUri, HttpMethod.POST, request, String.class);
	}

}
