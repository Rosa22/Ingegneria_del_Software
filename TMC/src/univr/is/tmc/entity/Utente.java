package univr.is.tmc.entity;

import univr.is.tmc.database.MyDriver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utente {


	private String nome;	
	private String cognome;
	private String password;
	private String email;
	private String telefono;
	private char privilegi;

	// ============== COSTRUTTORI ====================================================

	public Utente(String nome, String cognome, String password, String email, String telefono, char privilegi) 
	{
		this.nome = nome;
		this.cognome = cognome;
		this.password = password;
		this.email = email;
		this.telefono = telefono;
		this.privilegi = privilegi;
	}

	public Utente(ResultSet rs) throws SQLException 
	{
		this.nome = rs.getString("nome");
		this.cognome = rs.getString("cognome");
		this.password = rs.getString("pwd");
		this.email = rs.getString("mail");
		this.telefono = rs.getString("tel");
		this.privilegi = rs.getString("tipo").charAt(0);
	}

	public Utente() 
	{
		this.privilegi = 'U';
	}

	// ============== METODI ==========================================================

	public static Utente getUserData(String email) 
	{
		Utente res = null;
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "SELECT * FROM Utente WHERE mail = ?";
			Object[] params = new Object[1];
			params[0] = email;
			ResultSet rs = driver.execute(query, params);
			if (rs.next())
				res = new Utente(rs);
		} catch (SQLException e) {
			System.out.println("Select fallita! " + e);
		}
		return res;
	}

	public static List<Utente> getUsers() 
	{
		List<Utente> res = new ArrayList<>();
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "SELECT * FROM Utente ";
			ResultSet rs = driver.execute(query, null);
			while (rs.next())
				res.add(new Utente(rs));
		} catch (SQLException e) {
			System.out.println("Select fallita! " + e);
		}
		return res;
	}

	public static boolean inserisciUtente(String email, String nome, String cognome, String password, char privilegi, String telefono) 
	{
		boolean res = false;
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "INSERT INTO Utente ( mail, nome, cognome, pwd, tipo, tel ) VALUES ( ?, ?, ?, ?, ?, ?)";
			Object[] params = new Object[6];
			params[0] = email;
			params[1] = nome;
			params[2] = cognome;
			params[3] = password;
			params[4] = ""+privilegi;
			params[5] = telefono;
			// Se modifica 1 riga allora è andato a buon fine
			if (driver.update(query, params) == 1)
				res = true;
		} catch (SQLException e) {
			res = false;
			System.out.println("Select faillita! " + e);
		}
		return res;
	}

	public static boolean modificaUtente(String email, String nome, String cognome, String password, char privilegi, String telefono) 
	{
		boolean res = false;
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "UPDATE Utente SET nome = ?, cognome = ?, pwd = ?, tipo = ?, tel = ? WHERE mail = ?";
			Object[] params = new Object[6];
			params[0] = nome;
			params[1] = cognome;
			params[2] = password;
			params[3] = ""+privilegi;
			params[4] = telefono;
			params[5] = email;
			// Se modifica 1 riga allora è andato a buon fine
			if (driver.update(query, params) == 1)
				res = true;
		} catch (SQLException e) {
			res = false;
			System.out.println("Select faillita! " + e);
		}
		return res;
	}

	public static boolean eliminaUtente(String email) {
		boolean res = false;
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "DELETE FROM Utente WHERE mail = ?";
			Object[] params = new Object[1];
			params[0] = email;
			// Se modifica 1 riga allora è andato a buon fine
			if (driver.update(query, params) == 1)
				res = true;
		} catch (SQLException e) {
			System.out.println("Select faillita! " + e);
		}
		return res;
	}

	public static boolean trovaUtente(String email) {
		boolean res = false;
		try {
			MyDriver driver = MyDriver.getInstance();
			String query = "SELECT * FROM Utente WHERE mail = ?";
			Object[] params = new Object[1];
			params[0] = email;
			// Se modifica 1 riga allora è andato a buon fine
			//if (driver.update(query, params) == 1)
			ResultSet rs = driver.execute(query, params);
			if (rs.next())
				res = true;
		} catch (SQLException e) {
			System.out.println("Select faillita! " + e);
		}
		return res;
	}

	// =========== METODI SET/GET =============================================================

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public char getPrivilegi() {
		return privilegi;
	}

	public void setPrivilegi(char privilegi) {
		this.privilegi = privilegi;
	}
	
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}
