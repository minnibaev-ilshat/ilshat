package mail_service.minnibaev.com.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import common.com.minnibaev.dto.MailParams;
import mail_service.minnibaev.com.service.MailSenderService;

@Service
public class MailSenderServiceImpl implements MailSenderService {

	private final JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String emailFrom;
	@Value("${service.activation.uri}")
	private String activationServiceUri;

	public MailSenderServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void send(MailParams mailParams) {
		var subject = "Activate your profile ";
		var messageBody = getActivationMailBody(mailParams.getId());
		var emailTo = mailParams.getEmailTo();

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject(subject);
		mailMessage.setTo(emailTo);
		mailMessage.setFrom(emailFrom);
		mailMessage.setText(messageBody);

		javaMailSender.send(mailMessage);

	}

	private String getActivationMailBody(String id) {
		var message = String.format("Click the link to activate your profile:\n%s", activationServiceUri);
		return message.replace("{id}", id);
	}

}
