package com.foot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.foot.entities.Matchs;
@RepositoryRestResource
public interface MatchsRepository extends JpaRepository<Matchs, String>{

	@Query(value="SELECT m FROM Matchs m WHERE (m.equipeIn LIKE ?1 or m.equipeOut LIKE ?1) and (m.saison LIKE ?2) Order By m.dateMatch DESC")
	List<Matchs> findMatchsByEquipeInOrOutAndSaison(String equipe,String saison);
	
	@Query(value="SELECT m FROM Matchs m WHERE m.dateMatch > ?1 Order By m.dateMatch ASC")
	List<Matchs> findMatchsordered(String date);
	
	@Query(value="SELECT m FROM Matchs m  WHERE (m.equipeIn LIKE ?1 and m.dateMatch < ?2 ) Order By m.dateMatch DESC")
	List<Matchs> findLast7MatchsIn(String name,String date);
	
	@Query(value="SELECT m FROM Matchs m  WHERE (m.equipeOut LIKE ?1 and m.dateMatch < ?2 ) Order By m.dateMatch DESC")
	List<Matchs> findLast7MatchsOut(String name,String date);
}
