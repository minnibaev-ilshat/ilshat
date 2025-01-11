package node.com.minnibaev.service;

import common.com.minnibaev.entity.AppUser;

public interface AppUserService {
	String registerUser(AppUser appUser);

	String setEmail(AppUser appUser, String email);
}
