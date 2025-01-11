package mail_service.minnibaev.com.service;

import common.com.minnibaev.dto.MailParams;

public interface MailSenderService {
	void send(MailParams mailParams);
}
