package ilinet.com.minnibaev.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ilinet.com.minnibaev.entity.AppUser;

public interface AppUserRep extends JpaRepository<AppUser, Long> {
	AppUser findAppUserByTelegramUserId(Long id);
}
