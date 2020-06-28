package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private Map<String, Team> idMapTeams;
	private Map<Integer, Season> idMapSeasons;
	private Graph<Season, DefaultWeightedEdge> grafo;
	private List<PuntiStagione> best;
	private int max;
	private Map<Season, PuntiStagione> stagioniConsecutive;
	private List<PuntiStagione> list;
	
	public Model() {
		this.dao = new SerieADAO();
		this.idMapTeams = new HashMap<>();
		this.idMapSeasons  = new HashMap<>();
	}
	
	public List<Team> getTeams(){
		return dao.listTeams(idMapTeams);
	}
	
	public List<PuntiStagione> getpuntiStagione(Team t){
		dao.listAllSeasons(idMapSeasons);
		this.stagioniConsecutive = new HashMap<>();
		stagioniConsecutive = dao.getPuntiStagione(idMapSeasons, t);
		list = new ArrayList<>(stagioniConsecutive.values());
		Collections.sort(list);
		return list;
	}
	
	public void creaGrafo(Team t) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		List<PuntiStagione> list = new ArrayList<>(this.getpuntiStagione(t));
		
		for(PuntiStagione p1: list) {
			for(PuntiStagione p2: list) {
				if(p1 != p2) {
					if(!grafo.containsVertex(p1.getS()))
						grafo.addVertex(p1.getS());
					if(!grafo.containsVertex(p2.getS()))
						grafo.addVertex(p2.getS());
					if(p1.getPunti() > p2.getPunti())
						Graphs.addEdge(grafo, p2.getS(), p1.getS(), p1.getPunti()-p2.getPunti());
					if(p1.getPunti() < p2.getPunti())
						Graphs.addEdge(grafo, p1.getS(), p2.getS(), p2.getPunti()-p1.getPunti());
				}
			}
		}
	}
	
	public PuntiStagione getAnnataDoro() {
		PuntiStagione p = null;
		int max = 0;
		for(Season s: grafo.vertexSet()) {
			int sommaE = 0;
			int sommaU = 0;
			for(DefaultWeightedEdge e: grafo.incomingEdgesOf(s))
				sommaE += grafo.getEdgeWeight(e);
			for(DefaultWeightedEdge u: grafo.outgoingEdgesOf(s))
				sommaU += grafo.getEdgeWeight(u);
			int calcolo = sommaE - sommaU;
			if(calcolo > max) {
				max = calcolo;
				p = new PuntiStagione(s, calcolo);
			}
		}
		return p;
	}
	
	public List<PuntiStagione> trovPercorso(){
		this.best = new ArrayList<>();
		List<PuntiStagione> parziale = new ArrayList<>();
		this.max = 0;
		for(Season s:grafo.vertexSet()) {
			PuntiStagione daAggiungere = stagioniConsecutive.get(s);
			parziale.add(daAggiungere);
			cerca(parziale);
			parziale.remove(daAggiungere);
		}
		return best;
	}
	
	private void cerca(List<PuntiStagione> parziale) {
		if(parziale.size() > max) {
			this.best = new ArrayList<>(parziale);
			max = parziale.size();
		}
		
		for(Season s: grafo.vertexSet()) {
			PuntiStagione daAggiungere = stagioniConsecutive.get(s);
			if(!parziale.contains(daAggiungere)) {
				PuntiStagione ultimo = parziale.get(parziale.size()-1);
				if(list.indexOf(ultimo)+1 == list.indexOf(daAggiungere) && ultimo.getPunti()<daAggiungere.getPunti()) {
					parziale.add(daAggiungere);
					cerca(parziale);
					parziale.remove(daAggiungere);
				}
			}
		}
	}

	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}

}
