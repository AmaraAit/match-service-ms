package com.foot.web;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.foot.entities.Equipe;
import com.foot.entities.Matchs;
import com.foot.entities.MatchsJoue;
import com.foot.metier.IMatchsService;

@RestController
public class MatchsController {
	IMatchsService iMatchsService;

	public MatchsController(IMatchsService iMatchsService) {
		super();
		this.iMatchsService = iMatchsService;
	}
	@RequestMapping(value = "/insert",method = RequestMethod.GET)
	public List<Matchs> getMatchs() throws Exception{
		return iMatchsService.insertMatchs();
	}
	
	
	@RequestMapping(value = "/{equipe}",method = RequestMethod.GET)
	public List<Matchs> getMatchsByEquipeInAndSaison(@PathVariable String equipe){
		
		return iMatchsService.findMatchsJouerByEquipeInAndSaison(equipe);
		
	}
	@RequestMapping(value = "/Stat/{equipe}",method = RequestMethod.GET)
	public Equipe getStatByEquipeInAndSaison(@PathVariable String equipe){
		
		return iMatchsService.getStatByEquipe(equipe);
		
	}
}
