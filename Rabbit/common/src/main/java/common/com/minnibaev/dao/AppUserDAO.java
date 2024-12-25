package common.com.minnibaev.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import common.com.minnibaev.entity.AppUser;


public interface AppUserDAO extends JpaRepository<AppUser, Long> {

	AppUser findByTelegramUserId(Long id);

}
