package univr.is.tmc.entity;

import univr.is.tmc.database.MyDriver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class Furto {
	
	private int id;
	private String targa;
	private String data;
	private String ora;
	private List<Posizione> posizioni = new ArrayList<>();

	// Delay passato il quale un furto Attivo scade ed e' disponibile solo nello storico
    private static int TEMP = 10;

	// ============== COSTRUTTORI ====================================================

	public Furto(int id, String targa, String data, String ora) 
	{
		this.id = id;
		this.targa = targa;
		this.data = data;
		this.ora = ora;
	}

	public Furto(ResultSet rs) throws SQLException 
	{
		this.id = rs.getInt("id");
		this.targa = rs.getString("targa");
		this.data = rs.getString("data");
		this.ora = rs.getTime("ora").toString();
	}

	public Furto() { }

	// ============== METODI ==========================================================

	/**
	 * Ritorno una List di Furto contenente le righe rappresentanti
	 * i Furti, ANCORA ATTIVI, dei veicoli legati all'utente "email"
	 *
	 * @param email Email dell'utente in questione
	 * @return Lista di Furti Attivi dei veicoli dell'utente in questione
	 */

	public static List<Furto> getFurtiAttiviUtente(String email) 
	{	
		List<Furto> res = new ArrayList<>();
		List<Veicolo> veicoli = Veicolo.getVeicoliUser( email );        // lista Veicoli dell'Utente
		String data = getLocalData();
		String ora = getLocalOra();
		try {
			MyDriver driver = MyDriver.getInstance();
			
			for (Veicolo v : veicoli){
				
				String query = "SELECT f.id, f.targa, f.data, f.ora FROM Furto f JOIN Posizione p ON f.id = p.id_furto "+
								"WHERE f.id = ( SELECT MAX(id) FROM Furto where targa = ? ) "+
								"AND p.id = ( SELECT MAX(id) FROM Posizione WHERE targa = ? ) "+
								"AND f.data = ? "+
								"AND p.ora >= ?";
				Object[] params = new Object[4];
				params[0] = v.getTarga();
				params[1] = v.getTarga();
				params[2] = java.sql.Date.valueOf(data);
				params[3] = java.sql.Time.valueOf(ora);	

				ResultSet rs = driver.execute(query, params);

				while (rs.next())
					res.add(new Furto(rs));
			}
		} catch (SQLException e) {
			System.out.println("Select fallita! " + e);
		}
		return res;
	}
  
	/**
	 * Ritorno una List di Furto contenente le righe rappresentanti
	 * i Furti dei veicoli legati all'utente "email"
	 *
	 * @param email Email dell'utente in questione
	 * @return Lista di Furti nello Storico dei veicoli dell'utente in questione
	 */

	public static List<Furto> getStoricoFurtiUtente(String email) 
	{	
		List<Furto> res = new ArrayList<>();
		List<Veicolo> veicoli = Veicolo.getVeicoliUser( email );
		try {
			MyDriver driver = MyDriver.getInstance();
			
			for (Veicolo v : veicoli){

				String query = "SELECT id, targa, data, ora FROM Furto where targa = ? ORDER BY(data)";
				Object[] params = new Object[1];
				params[0] = v.getTarga();

				ResultSet rs = driver.execute(query, params);

				while (rs.next())
					res.add(new Furto(rs));
			}
		} catch (SQLException e) {
			System.out.println("Select fallita! " + e);
		}
		return res;
	}

	/**
	 * Ritorno il Furto appena inserito per ricavarvi l'Id
	 *
	 * @param targa Targa veicolo in questione
	 * @param data Data del furto in questione
	 * @param ora di inizio del furto in questione
	 * @return Furto appena inserito
	 */

    public static Furto inserisciFurto(String targa, String data, String ora) 
	{
		boolean res = false;
        Furto f = null;
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "INSERT INTO Furto ( targa, data, ora ) VALUES ( ?, ?, ? )";
			Object[] params = new Object[3];
			params[0] = targa;
			params[1] = java.sql.Date.valueOf(data);
			params[2] = java.sql.Time.valueOf(ora);
			// Se va a buon fine
			if (driver.update(query, params) == 1)
				res = true;

            if (res){
                query = "SELECT * FROM Furto WHERE targa = ? AND data = ? AND ora = ?";
                ResultSet rs = driver.execute(query, params);
                while (rs.next())
			        f = new Furto(rs);
            }

		} catch (SQLException e) {
			res = false;
			System.out.println("Select faillita! " + e);
		}
        return f;
	}

	/**
	 * Ricava e ritorna la Data Corrente
	 *
	 * @return Data corrente in String
	 */

	private static String getLocalData()
	{
		Calendar localDate = Calendar.getInstance();
		String data = "", mese = "", giorno = "";

		if ( (localDate.get(Calendar.MONTH)+1) < 10 )					// MESE
			mese = "0"+(localDate.get(Calendar.MONTH)+1);
		else 
			mese = ""+(localDate.get(Calendar.MONTH)+1);

		if ( localDate.get(Calendar.DAY_OF_MONTH) < 10 )				// GIORNO
			giorno = "0"+ localDate.get(Calendar.DAY_OF_MONTH);
		else 
			giorno = ""+ localDate.get(Calendar.DAY_OF_MONTH);
	
		return data = localDate.get(Calendar.YEAR) +"-"+ mese +"-"+ giorno;
	}
	
	/**
	 * Ricava e ritorna Ora corrente - Delay 
	 *
	 * @return Ora corrente-TEMP
	 */

	private static String getLocalOra()
	{
		Calendar localDate = Calendar.getInstance();
		localDate.add(Calendar.MINUTE, -TEMP);
		String ora = "";
		
		if ( localDate.get(Calendar.HOUR_OF_DAY) < 10 )
			ora = "0"+ localDate.get(Calendar.HOUR_OF_DAY);
		else 
			ora = ""+ localDate.get(Calendar.HOUR_OF_DAY);

		if ( localDate.get(Calendar.MINUTE) < 10 )
			ora = ora+":0"+ localDate.get(Calendar.MINUTE);
		else
			ora = ora+":"+ localDate.get(Calendar.MINUTE);
		
		return ora + ":00";		
	}

	// =========== METODI SET/GET =============================================================

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTarga() {
		return targa;
	}

	public void setTarga(String targa) {
		this.targa = targa;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}
	
	public static String getLocalDate() {
		return ""+getLocalData()+" "+getLocalOra();
	}
}
