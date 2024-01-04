package com.foot.metier;

import java.util.List;

import com.foot.entities.Equipe;
import com.foot.entities.Matchs;


public interface IMatchsService {
	
	public List<Matchs> insertMatchs();
	public Matchs updateMatchs(String id);
	public String deleteMatchsMessage(String id);
	public Matchs deleteMatchs(String id);
	public Matchs insertMatchs(Matchs m);
	
	
	public List<Matchs> misajour();
	
	public List<Matchs> getNextMatchs();
	
	public List<Matchs> getLast7MatchsIn(String name,String date);
	public List<Matchs> getLast7MatchsOut(String name,String date);
	
	public List<Matchs> getPronostic();
	
	
	
	public String deleteMatchsJoueMessage(String id);
	
	public List<Matchs> findMatchsByEquipeAndSaison(String equipe,String saison);
	public List<Matchs> findMatchsJouerByEquipeInAndSaison(String equipe);
	
	
	public Equipe getStatByEquipe(String name);
	public Equipe getStatByEquipeAndDate(String name,String date);

}
