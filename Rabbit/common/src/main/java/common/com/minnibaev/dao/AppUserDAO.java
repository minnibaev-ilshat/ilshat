package common.com.minnibaev.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import common.com.minnibaev.entity.AppUser;

import java.util.Optional;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByTelegramUserId(Long id);

}
