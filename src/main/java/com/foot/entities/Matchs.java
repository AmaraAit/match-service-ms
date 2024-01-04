package com.foot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Matchs {
	@Id
	protected String id;
	protected String equipeIn;
	@Transient
	protected Equipe equipeDom;
	protected String equipeOut;
	@Transient
	protected Equipe equipeExt;
	protected String dateMatch;
	protected String championnat;
	protected String saison;
	protected int bEI;
	protected int bEO;
	protected String url;

}
