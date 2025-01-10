package common.com.minnibaev.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import common.com.minnibaev.entity.AppUser;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByTelegramUserId(Long id);
	Optional<AppUser> findById(Long id);
	Optional<AppUser> findByEmail(String email);

}
