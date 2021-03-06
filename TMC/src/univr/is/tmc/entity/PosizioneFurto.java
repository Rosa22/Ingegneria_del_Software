package univr.is.tmc.entity;

import univr.is.tmc.database.MyDriver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PosizioneFurto {

	private int id;
	private String ora;
	private String latitudine;	
	private String longitudine;
	private String velocita;

	// ============== COSTRUTTORI ====================================================

	public PosizioneFurto(int id, String ora, String latitudine, String longitudine) 
	{
		this.id = id;
		this.ora = ora;
		this.latitudine = latitudine;
		this.longitudine = longitudine;
		//this.velocita = velocita;
	}

	public PosizioneFurto(ResultSet rs) throws SQLException 
	{
		this.id = rs.getInt("id");
		this.ora = rs.getTime("ora").toString();
		this.latitudine = rs.getString("latitudine");
		this.longitudine = rs.getString("longitudine");
		//this.velocita = rs.getString("velocita");
	}

	public PosizioneFurto() {}

	// ============== METODI ==========================================================

	/**
	 * Ritorna List PosizioneFurto rappresentante tutte le posizioni del furto indicato
	 *
	 * @param idFurto ID del furto in questione
	 * @return List di PosizioneFurto indicante le posizioni del furto in questione
	 */

	public static List<PosizioneFurto> getTravel(int idFurto) 
	{
		List<PosizioneFurto> res = new ArrayList<>();
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "SELECT p.id, p.ora, p.latitudine, p.longitudine FROM Posizione p JOIN Furto f ON f.id = p.id_furto "+
							"WHERE f.id = ?";
			Object[] params = new Object[1];
			params[0] = idFurto;
			ResultSet rs = driver.execute(query, params);
			while (rs.next())
				res.add(new PosizioneFurto(rs));
		} catch (SQLException e) {
			System.out.println("Select fallita! " + e);
		}
		return res;
	}

	/**
	 * Ritorna l'ultima PosizioneFurto del furto indicato
	 *
	 * @param idFurto ID del furto in questione
	 * @return PosizioneFurto indicante l'ultima Posizione del furto in questione
	 */

	public static PosizioneFurto getUltimaPos(int idFurto) 
	{
		PosizioneFurto res = null;
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "SELECT p.id, p.ora, p.latitudine, p.longitudine FROM Posizione p JOIN Furto f ON f.id = p.id_furto "+
							"WHERE f.id = ? AND p.id = ( SELECT max(id) FROM Posizione WHERE id_furto = ?)";
			Object[] params = new Object[2];
			params[0] = idFurto;
			params[1] = idFurto;
			ResultSet rs = driver.execute(query, params);
			if (rs.next())
				res = new PosizioneFurto(rs);
		} catch (SQLException e) {
			System.out.println("Select fallita! " + e);
		}
		return res;
	}

	/**
	 * Inserisco la posizione attuale del veicolo e furto in questiome
	 *
	 * @param idFurto ID Furto in questione
	 * @param latitudine Latitudine attuale
	 * @param longitudine Longitudine attuale
	 * @param ora Ora ultima localizzazione
	 * @param velocita Velocita' attuale del veicolo
	 * @return boolean che indica il successo dell'azione di INSERT
	 */

    public static boolean inserisciPosizione(int idFurto, double latitudine, double longitudine, String ora, int velocita) 
	{
		boolean res = false;
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "INSERT INTO Posizione ( id_furto, latitudine, longitudine, ora, velocita ) VALUES ( ?, ?, ?, ?, ? )";
			Object[] params = new Object[5];
			params[0] = idFurto;
			params[1] = latitudine;
			params[2] = longitudine;
			params[3] = java.sql.Time.valueOf(ora);
			params[4] = velocita;
			// Se modifica 1 riga allora è andato a buon fine
			if (driver.update(query, params) == 1)
				res = true;
		} catch (SQLException e) {
			res = false;
			System.out.println("Select faillita! " + e);
		}
		return res;
	}
	
	// =========== METODI SET/GET =============================================================

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}

	public String getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}

	public String getVelocita() {
		return velocita;
	}

	public void setVelocita(String velocita) {
		this.velocita = velocita;
	}
}
