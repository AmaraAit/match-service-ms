package com.foot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import com.foot.entities.MatchsJoue;
@RepositoryRestResource
public interface MatchsJoueRepository extends JpaRepository<MatchsJoue, Long>{
	@Query(value="SELECT m FROM Matchs m WHERE (m.equipeIn LIKE ?1 or m.equipeOut LIKE ?1) and (m.saison LIKE ?2) ")
	List<MatchsJoue> findMatchsJoueByEquipeInOrOutAndSaison(String equipe,String saison);

}
