package common.com.minnibaev.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import common.com.minnibaev.entity.AppPhoto;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {

}
