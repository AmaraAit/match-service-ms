package com.foot.metier;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.foot.dao.MatchsJoueRepository;
import com.foot.dao.MatchsRepository;
import com.foot.entities.Equipe;
import com.foot.entities.Matchs;
import com.foot.entities.MatchsJoue;



@Service
public class MatchsServiceImp implements IMatchsService{
	MatchsRepository matchsRepository;
	MatchsJoueRepository joueRepository;
	
	

	public MatchsServiceImp(MatchsRepository matchsRepository, MatchsJoueRepository joueRepository) {
		super();
		this.matchsRepository = matchsRepository;
		this.joueRepository = joueRepository;
	}

	public List<Matchs> insertMatchs(String url,String saison,String league) {
		
		List<Matchs> listMatch=new ArrayList<>();
		try {
			
			Document doc = Jsoup.connect(url).get();
			Elements newsHeadlines = doc.select("table.stats_table");
			String d=null;
			String score;
			int bEI=-1;
			int bEO=-1;
			for (Element headline : newsHeadlines.select("tr")) {
				d=headline.select("td[data-stat=date]").text()+" "+headline.select("td[data-stat=start_time]").text();
				score=headline.select("td[data-stat=score]").text();
				
				if(score!="" && !score.contains("(")) {
					String a="–";
					String b=" ";
				    score=score.replace(a, b);
					
					String [] but=score.split(b);
					
					if(but.length>1) {
						bEI=Integer.parseInt(but[0]);
						bEO=Integer.parseInt(but[1]);
						
						if(d!="") {
						MatchsJoue m=new MatchsJoue();
						m.setDateMatch(d);
						m.setBEI(bEI);
						m.setBEO(bEO);
						m.setEquipeIn(headline.select("td[data-stat=home_team]").text());
						m.setEquipeOut(headline.select("td[data-stat=away_team]").text());
						m.setSaison(saison);
						
						m.setChampionnat(league);
						joueRepository.save(m);
						}
					}
				}else if(headline.select("td[data-stat=home_team]").text()!=""){
					Matchs m=new Matchs();
					m.setDateMatch(d);
					m.setEquipeIn(headline.select("td[data-stat=home_team]").text());
					m.setEquipeOut(headline.select("td[data-stat=away_team]").text());
					m.setSaison(saison);
					m.setChampionnat(league);
					matchsRepository.save(m);
					listMatch.add(m);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listMatch;
	}

	@Override
	public List<Matchs> insertMatchs() {
		String url=null;
		String[] saisons= {"2010-2011",	"2011-2012","2012-2013",
				"2013-2014","2014-2015","2015-2016","2016-2017",
				"2017-2018","2018-2019","2019-2020","2020-2021",
				"2021-2022","2022-2023","2023-2024"};
	    int [] index= {9,12,20,11,13,23,32,37,8,19};
	    String[] championnat= {"Premier-League","La-Liga","Bundesliga","Serie-A",
	    		"Ligue-1","Eredivisie","Primeira-Liga","Belgian-Pro-League",
	    		"Champions-League","Europa-League"};
	    String com="/calendrier/Calendrier-et-resultats-";
	    String h2tp="https://fbref.com/fr/comps/";
	    List<Matchs> nextMatch=new ArrayList<>();
	    int i=0;
		for(String ch : championnat) {
			for (String s : saisons) {
				url=h2tp+index[i]+"/"+s+com+s+"-"+ch;
				for (Matchs matchsNext : insertMatchs(url, s,ch)) {
					nextMatch.add(matchsNext);
				}
					
			}
			i+=1;
		}
		return nextMatch;
	}



	@Override
	public Matchs updateMatchs(long id) {
		Matchs m = matchsRepository.getById(id);
		
		return m;
	}



	@Override
	public String deleteMatchsMessage(long id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Matchs deleteMatchs(long id) {
		matchsRepository.deleteById(id);
		return null;
	}



	@Override
	public Matchs insertMatchs(Matchs m) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<MatchsJoue> insertMatchsJour() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public MatchsJoue updateMatchsJoue(long id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String deleteMatchsJoueMessage(long id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public MatchsJoue deleteMatchsJoue(long id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public MatchsJoue insertMatchsJoue(MatchsJoue m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Matchs> findMatchsByEquipeAndSaison(String equipe, String saison) {
		return null;
	}

	@Override
	public List<Matchs> findMatchsJouerByEquipeInAndSaison(String equipe) {
		List<Matchs> listeMatchs= new ArrayList<>();
		for (Matchs matchs : matchsRepository.findMatchsByEquipeInOrOutAndSaison(equipe, "2023-2024")) {
			String pattern = "yyyy-MM-dd HH:mm:ss";

			// Create an instance of SimpleDateFormat used for formatting 
			// the string representation of date according to the chosen pattern
			DateFormat df = new SimpleDateFormat(pattern);

			// Get the today date using Calendar object.
			Date today = Calendar.getInstance().getTime();        
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String todayAsString = df.format(today);

			// Print the result!
			System.out.println("Today is: " + todayAsString);
			if(matchs.getDateMatch().compareTo(todayAsString)<0) {
				listeMatchs.add(matchs);
			}
			
		}
		
		return listeMatchs;
		
	}

	@Override
	public Equipe getStatByEquipe(String name) {
		List<MatchsJoue> listeMatchs= new ArrayList<>();
		double p;
		String c = null;
		int i=0;
		for (MatchsJoue matchs : joueRepository.findMatchsJoueByEquipeInOrOutAndSaison(name, "2023-2024")) {
			String pattern = "yyyy-MM-dd HH:mm:ss";

			// Create an instance of SimpleDateFormat used for formatting 
			// the string representation of date according to the chosen pattern
			DateFormat df = new SimpleDateFormat(pattern);

			// Get the today date using Calendar object.
			Date today = Calendar.getInstance().getTime();        
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String todayAsString = df.format(today);
			
			// Print the result!
			System.out.println("Today is: " + todayAsString);
			if(matchs.getBEI()>0 && matchs.getBEO()>0) {
				i+=1;
				listeMatchs.add(matchs);
			}
			c=matchs.getChampionnat();
		}
		Random r = new Random();
		int id= r.nextInt((100000 - 0) + 1) + 0;
		Equipe e=Equipe.builder()
				.name(name)
				.championnat(c)
				.id(id)
				.deuxEM(i/listeMatchs.size())
				.build();
		
		return e;
	}

	@Override
	public List<MatchsJoue> findMatchsJoueByEquipeAndSaison(String equipe, String saison) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MatchsJoue> findMatchsJoueByEquipeInAndSaison(String equipe, String saison) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
