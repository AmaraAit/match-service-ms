package com.foot.entities;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Equipe {
	@Id
	int id;
	String name;
	String championnat;
	double deuxEMD;
	double deuxEME;
	double matchNULLD;
	double matchNULLE;
	double matchGAGNED;
	double matchGAGNEE;
	double matchPERDUD;
	double matchPERDUE;
	int nombreBUTMARQUE;
	int nombreBUTENCAISSE;
	int nombreMATCHMARQUE;
	int nombreMATCHENCAISSE;
	int nombreTOTALMATCHS;
	

}
