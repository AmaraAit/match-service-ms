package com.foot.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter

@ToString
public class MatchsGo extends Matchs{
	
	protected int bEI;
	protected int bEO;
	protected String url;

	public MatchsGo() {
		super();
		
	}
	public MatchsGo(String id, String equipeIn, Equipe equipeDom, String equipeOut, Equipe equipeExt, String dateMatch,
			String championnat, String saison ,int bEI,int bEO,String url) {
		
		super(id, equipeIn, equipeDom, equipeOut, equipeExt, dateMatch, championnat, saison);
	}
	
	

}
