package common.com.minnibaev.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import common.com.minnibaev.entity.AppDocument;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long>{

}
