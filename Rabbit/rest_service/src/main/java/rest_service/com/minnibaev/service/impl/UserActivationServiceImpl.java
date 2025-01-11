package rest_service.com.minnibaev.service.impl;

import org.springframework.stereotype.Service;

import common.com.minnibaev.dao.AppUserDAO;
import common.com.minnibaev.utils.CryptoTool;
import rest_service.com.minnibaev.service.UserActivationService;

@Service
public class UserActivationServiceImpl implements UserActivationService {

	private final AppUserDAO appUserDAO;
	private final CryptoTool cryptoTool;

	public UserActivationServiceImpl(AppUserDAO appUserDAO, CryptoTool cryptoTool) {
		this.appUserDAO = appUserDAO;
		this.cryptoTool = cryptoTool;
	}

	@Override
	public boolean activation(String cryptoUserId) {
		var userId = cryptoTool.idOf(cryptoUserId);
		var optional = appUserDAO.findById(userId);
		if (optional.isPresent()) {
			var user = optional.get();
			user.setIsActive(true);
			appUserDAO.save(user);
			return true;
		}
		return false;
	}

}
