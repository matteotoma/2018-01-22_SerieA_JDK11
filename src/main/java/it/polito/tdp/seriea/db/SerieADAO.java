package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.PuntiStagione;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public void listAllSeasons(Map<Integer, Season> idMapSeasons) {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = new Season(res.getInt("season"), res.getString("description"));
				result.add(s);
				idMapSeasons.put(s.getSeason(), s);
			}

			conn.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Team> listTeams(Map<String, Team> idMap) {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team t = new Team(res.getString("team"));
				result.add(t);
				idMap.put(t.getTeam(),t);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<Season, PuntiStagione> getPuntiStagione(Map<Integer, Season> idMap,Team t){
		String sql = "SELECT m.HomeTeam AS h, m.AwayTeam a, m.FTR r, m.Season s " + 
				"FROM matches AS m " + 
				"WHERE (m.HomeTeam=? OR m.AwayTeam=? )";
		Map<Season, PuntiStagione> map = new HashMap<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = idMap.get(res.getInt("s"));
				
				if(!map.containsKey(s)) {
					PuntiStagione p = new PuntiStagione(s, 0);
					map.put(p.getS(), p);
				}
					
				// vince in casa
				if(res.getString("h").equals(t.getTeam()) && res.getString("r").equals("H")) 
					map.get(s).incremento(3);
					
				// vince in trasferta
				if(res.getString("a").equals(t.getTeam()) && res.getString("r").equals("A")) 
					map.get(s).incremento(3);
					
				// pareggio
				if(res.getString("r").equals("D"))
					map.get(s).incremento(1);					
			}
			
			conn.close();
			return map;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

