package com.foot.metier;

import java.util.List;

import com.foot.entities.Equipe;
import com.foot.entities.Matchs;
import com.foot.entities.MatchsJoue;

public interface IMatchsService {
	
	public List<Matchs> insertMatchs();
	public Matchs updateMatchs(long id);
	public String deleteMatchsMessage(long id);
	public Matchs deleteMatchs(long id);
	public Matchs insertMatchs(Matchs m);
	
	public List<MatchsJoue> insertMatchsJour();
	public MatchsJoue updateMatchsJoue(long id);
	public String deleteMatchsJoueMessage(long id);
	public MatchsJoue deleteMatchsJoue(long id);
	public MatchsJoue insertMatchsJoue(MatchsJoue m);
	
	public List<Matchs> findMatchsByEquipeAndSaison(String equipe,String saison);
	public List<Matchs> findMatchsJouerByEquipeInAndSaison(String equipe);
	public List<MatchsJoue> findMatchsJoueByEquipeAndSaison(String equipe,String saison);
	public List<MatchsJoue> findMatchsJoueByEquipeInAndSaison(String equipe,String saison);
	
	public Equipe getStatByEquipe(String name);

}
