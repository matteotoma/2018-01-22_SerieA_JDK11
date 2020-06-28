package it.polito.tdp.seriea.model;

public class PuntiStagione implements Comparable<PuntiStagione>{
	
	private Season s;
	private Integer punti;
	
	public PuntiStagione(Season s, Integer punti) {
		super();
		this.s = s;
		this.punti = punti;
	}

	public Season getS() {
		return s;
	}

	public void setS(Season s) {
		this.s = s;
	}

	public Integer getPunti() {
		return punti;
	}

	public void setPunti(Integer punti) {
		this.punti = punti;
	}
	
	public void incremento(Integer punti) {
		this.punti += punti;
	}

	@Override
	public int compareTo(PuntiStagione o) {
		return this.s.getSeason() - o.getS().getSeason();
	}

}
