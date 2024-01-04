package com.foot.metier;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.foot.dao.MatchsRepository;
import com.foot.entities.Equipe;
import com.foot.entities.Matchs;




@Service
public class MatchsServiceImp implements IMatchsService{
	MatchsRepository matchsRepository;
	
	
	

	public MatchsServiceImp(MatchsRepository matchsRepository) {
		
		this.matchsRepository = matchsRepository;
		
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
				Elements scoreurl=headline.select("td[data-stat=score]");
				score=headline.select("td[data-stat=score]").text();;
				if(score!="" && !score.contains("(")) {
					String a="–";
					String b=" ";
				    score=score.replace(a, b);
					
					String [] but=score.split(b);
					
					if(but.length>1) {
						bEI=Integer.parseInt(but[0]);
						bEO=Integer.parseInt(but[1]);
						System.out.println();
						if(d!="") {
						Matchs m=Matchs.builder()
								.id(UUID.randomUUID().toString())
								.equipeIn(headline.select("td[data-stat=home_team]").text())
								.equipeOut(headline.select("td[data-stat=away_team]").text())
								.url("https://fbref.com"+scoreurl.select("a").attr("href"))
								.equipeDom(getStatByEquipeAndDate(headline.select("td[data-stat=home_team]").text(), d))
								.equipeExt(getStatByEquipeAndDate(headline.select("td[data-stat=away_team]").text(), d))
								.dateMatch(d)
								.championnat(league)
								.saison(saison)
								.bEI(bEI)
								.bEO(bEO)
								.build();
						matchsRepository.save(m);
						}
					}
				}else if(headline.select("td[data-stat=home_team]").text()!=""){
					Matchs m=Matchs.builder()
							.id(UUID.randomUUID().toString())
							.equipeIn(headline.select("td[data-stat=home_team]").text())
							.equipeOut(headline.select("td[data-stat=away_team]").text())
							.url("https://fbref.com"+scoreurl.select("a").attr("href"))
							.dateMatch(d)
							.championnat(league)
							.saison(saison)
							.bEI(bEI)
							.bEO(bEO)
							.build();
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
		String[] saisons= {/*"2010-2011",	"2011-2012","2012-2013",
				"2013-2014","2014-2015","2015-2016",*/"2016-2017",
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
	public Matchs updateMatchs(String id) {
		Matchs m = matchsRepository.getById(id);
		m.setBEI(-1);
		m.setBEO(-1);
		
		return m;
	}



	@Override
	public String deleteMatchsMessage(String id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Matchs deleteMatchs(String id) {
		matchsRepository.deleteById(id);
		return null;
	}



	@Override
	public Matchs insertMatchs(Matchs m) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String deleteMatchsJoueMessage(String id) {
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

			// Get the today date using Calendar object
			Date today = Calendar.getInstance().getTime();        
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String todayAsString = df.format(today);

			
			if(matchs.getDateMatch().compareTo(todayAsString)<0) {
				listeMatchs.add(matchs);
			}
			
		}
		
		return listeMatchs;
		
	}

	@Override
	public Equipe getStatByEquipe(String name) {
		List<Matchs> listeMatchs= new ArrayList<>();
		List<Matchs> listeMatchsDOM= new ArrayList<>();
		List<Matchs> listeMatchsEXT= new ArrayList<>();
		double p;
		String c = null;
		int i=0;
		int totalMatchsJouer=0;
		for (Matchs matchs : matchsRepository.findMatchsByEquipeInOrOutAndSaison(name, "2023-2024")) {
			if(matchs.getBEI()>=0) {
				totalMatchsJouer += 1;
				if(matchs.getBEI()>0 && matchs.getBEO()>0) {
					i+=1;
					
					listeMatchs.add(matchs);
					}
			}
			
			c=matchs.getChampionnat();
		}
		double moyenDEM=listeMatchs.size()*100/totalMatchsJouer;
		

		Random r = new Random();
		int id= r.nextInt((100000 - 0) + 1) + 0;
		Equipe e=Equipe.builder()
				.name(name)
				.championnat(c)
				.id(id)
				.deuxEMD(moyenDEM)
				.build();
		
		return e;
	}
	
	@Override
	public List<Matchs> misajour() {
		List<Matchs> l=new ArrayList<>();
		for (Matchs m : matchsRepository.findAll()) {
			if(m.getUrl().equals("https://fbref.com")) {
				m.setBEI(-1);
				m.setBEO(-1);
				matchsRepository.save(m);
				l.add(m);
			}
			
		}
		
		return l;
	}

	@Override
	public List<Matchs> getNextMatchs() {
		
		List<Matchs> nextMatch=new ArrayList<>();
		int i=0;
		for (Matchs matchs : matchsRepository.findMatchsordered()) {
			if(matchs.getBEI()<0) {
				matchs.setEquipeDom(getStatByEquipeAndDate(matchs.getEquipeIn(), matchs.getDateMatch()));
				matchs.setEquipeExt(getStatByEquipeAndDate(matchs.getEquipeOut(), matchs.getDateMatch()));
				nextMatch.add(matchs);
				i += 1;
			}
			if(i>=30) {
				break;
			}
		}
		return nextMatch;
	}

	@Override
	public List<Matchs> getLast7MatchsIn(String name,String date) {
		int i=0;
		List<Matchs> l=new ArrayList<>();
		for (Matchs e : matchsRepository.findLast7MatchsIn(name, date)) {
			if(i<=6) {
				l.add(e);
				i += 1;
			}
		}
		return l;
	}

	@Override
	public List<Matchs> getLast7MatchsOut(String name,String date) {
		int i=0;
		List<Matchs> l=new ArrayList<>();
		for (Matchs e : matchsRepository.findLast7MatchsOut(name, date)) {
			if(i<=6) {
				l.add(e);
				i += 1;
			}
		}
		return l;
	}

	@Override
	public Equipe getStatByEquipeAndDate(String name, String date) {
		List<Matchs> listeMatchs= new ArrayList<>();
		List<Matchs> listeMatchsDOM= matchsRepository.findLast7MatchsIn(name, date);
		List<Matchs> listeMatchsEXT= matchsRepository.findLast7MatchsOut(name, date);
		double p;
		double deuxEMD = 0;
		double deuxEME = 0;
		double matchNULLD = 0;
		double matchNULLE = 0;
		double matchGAGNED = 0;
		double matchGAGNEE = 0;
		double matchPERDUD = 0;
		double matchPERDUE = 0;
		int nombreBUTMARQUE = 0;
		int nombreBUTENCAISSE = 0;
		int nombreMATCHMARQUE = 0;
		int nombreMATCHENCAISSE = 0;
		int nombreTOTALMATCHS = 0;
		String c = null;
		int i=0;
		int cont=0;
		for (Matchs matchs : listeMatchsDOM) {
			if(matchs.getBEI()>=0) {
				cont+=1;
				if(cont>6) {
					break;
				}
				nombreTOTALMATCHS += 1;
				if(matchs.getBEI()>0 ) {
					nombreBUTMARQUE+=matchs.getBEI();
					nombreMATCHMARQUE+=1;
					
				}
				if(matchs.getBEO()>0) {
					nombreBUTENCAISSE+=matchs.getBEO();
					nombreMATCHENCAISSE+=1;
					
				}
				if((matchs.getBEO()>0) && (matchs.getBEI()>0 )) {
					deuxEMD+=1.0;
				}
				
				if((matchs.getBEO() < matchs.getBEI() )) {
					matchGAGNED+=1.0;
				}else if((matchs.getBEO() == matchs.getBEI() )) {
					matchNULLD+=1.0;
				}else {
					matchPERDUD+=1.0;
				}
			}
			c=matchs.getChampionnat();
		}
	    cont=0;
		for (Matchs matchs : listeMatchsEXT) {
			System.out.println(matchs.toString());
			if(matchs.getBEO()>=0) {
				cont+=1;
				if(cont>6) {
					break;
				}
				nombreTOTALMATCHS += 1;
				if(matchs.getBEO()>0 ) {
					nombreBUTMARQUE+=matchs.getBEO();
					nombreMATCHMARQUE+=1;
					}
				if(matchs.getBEI()>0) {
					nombreBUTENCAISSE+=matchs.getBEI();
					nombreMATCHENCAISSE+=1;
				}
				if((matchs.getBEO()>0) && (matchs.getBEI()>0 )) {
					deuxEME+=1.0;
				}
				if((matchs.getBEO() > matchs.getBEI() )) {
					matchGAGNEE+=1.0;
				}else if((matchs.getBEO() == matchs.getBEI() )) {
					matchNULLE+=1.0;
				}else {
					matchPERDUE+=1.0;
				}
			}
			c=matchs.getChampionnat();
		}
		
		double moyenDEM=listeMatchs.size()*100/nombreTOTALMATCHS;
		

		Random r = new Random();
		int id= r.nextInt((100000 - 0) + 1) + 0;
		Equipe e=Equipe.builder()
				.name(name)
				.championnat(c)
				.id(id)
				.deuxEMD(deuxEMD)
				.deuxEME(deuxEME)
				.matchGAGNED(matchGAGNED)
				.matchGAGNEE(matchGAGNEE)
				.matchNULLD(matchNULLD)
				.matchNULLE(matchNULLE)
				.matchPERDUD(matchPERDUD)
				.matchPERDUE(matchPERDUE)
				.nombreBUTENCAISSE(nombreBUTENCAISSE)
				.nombreBUTMARQUE(nombreBUTMARQUE)
				.nombreMATCHENCAISSE(nombreMATCHENCAISSE)
				.nombreMATCHMARQUE(nombreMATCHMARQUE)
				.nombreTOTALMATCHS(nombreTOTALMATCHS)
				.build();
		
		return e;
	}

	@Override
	public List<Matchs> getPronostic() {
		List<Matchs> l= new ArrayList<>();
		for (Matchs matchs : getNextMatchs()) {
			if(matchs.getEquipeDom().getDeuxEMD()/matchs.getEquipeDom().getNombreTOTALMATCHS()>0.32) {
				if(matchs.getEquipeExt().getDeuxEMD()/matchs.getEquipeDom().getNombreTOTALMATCHS()>0.32) {
					
				}
			}
			
		}
		return null;
	}

	

	

}
