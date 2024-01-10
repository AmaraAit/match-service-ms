package com.foot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.foot.entities.Matchs;
import com.foot.entities.MatchsGo;
@RepositoryRestResource
public interface MatchsGoRepository extends JpaRepository<MatchsGo, String>{
		
	@Query(value="SELECT m FROM MatchsGo m WHERE (m.equipeIn LIKE ?1 or m.equipeOut LIKE ?1) and (m.saison LIKE ?2) Order By m.dateMatch DESC")
	List<MatchsGo> findMatchsByEquipeInOrOutAndSaison(String equipe,String saison);
	
	@Query(value="SELECT m FROM MatchsGo m  Order By m.dateMatch ASC")
	List<MatchsGo> findMatchsordered();
	
	@Query(value="SELECT m FROM MatchsGo m  WHERE (m.equipeIn LIKE ?1 and m.dateMatch < ?2 ) Order By m.dateMatch DESC")
	List<MatchsGo> findLast7MatchsIn(String name,String date);
	
	@Query(value="SELECT m FROM MatchsGo m  WHERE (m.equipeOut LIKE ?1 and m.dateMatch < ?2 ) Order By m.dateMatch DESC")
	List<MatchsGo> findLast7MatchsOut(String name,String date);
}
