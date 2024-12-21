package node.com.minnibaev.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import node.com.minnibaev.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long>{

}
