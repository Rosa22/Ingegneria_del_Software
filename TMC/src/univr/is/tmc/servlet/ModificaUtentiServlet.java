package univr.is.tmc.servlet;

import univr.is.tmc.entity.Utente;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModificaUtentiServlet extends HttpServlet {

	private static char utente = 'U';
	private static char admin = 'A';

    /**
	 * ModificaUtentiServlet, 
     * IF (azione = "Nuovo Utente")
     *      IF ( controllo dati = OK )
     *          IF ( utente esiste = NO )
     *              IF ( password = ripeti password )
     *                  inserisci utente
     * IF (azione = "Modifica")
     *      IF ( controllo dati = OK )
     *          IF ( password = ripeti password )
     *              modifica utente
     * vai a modificaEffettuata.jsp
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String azione = request.getSession().getAttribute("azione").toString();

		if (azione.equalsIgnoreCase("Nuovo Utente")) {
			// I dati nella form sono corretti?
			if (controllaDati(request))
				// Esiste già?
				if (this.trovaUtente(request))
					// Password corrispondono?
					if (this.controllaPassword(request))
						// Inserimento
						this.InsertUtente(request);
		}
		if (azione.equalsIgnoreCase("Modifica")) {
			// I dati nella form sono corretti?
			if (controllaDati(request))
				// Password corrispondono?
				if (this.controllaPassword(request))
					// Modifica
					this.UpdateUtente(request);
		}

		response.sendRedirect("modificaEffettuata.jsp");
	}

    /**
	 * Aggiorna l'Utente
	 *
	 * @param request contiene l'utente in questione e i suoi nuovi parametri
	 * @return boolean che indica il successo dell'azione dell'UPDATE
	 */

	private boolean UpdateUtente(HttpServletRequest request) {
		if (Utente.modificaUtente(
				request.getSession().getAttribute("utenteSel").toString(),
				request.getParameter("nome").toString(),
				request.getParameter("cognome").toString(),
				request.getParameter("password").toString(),
				request.getParameter("privilegi").toString().charAt(0),
				request.getParameter("telefono").toString())) {

			// Modifica effettuata con successo
			// Se ho modificato l'Utente corrente, aggiorno i suoi permessi
				if(request.getSession().getAttribute("utenteSel").toString().equals( 
					request.getSession().getAttribute("currUserEmail").toString() ) )
						request.getSession().setAttribute("currUserMode", request.getParameter("privilegi").toString().charAt(0) );
 
			request.getSession().setAttribute("messaggio", "Modifica effettuata con successo!");
			return true;
		} else {
			// Inserimento non andato a buon fine
			request.getSession().setAttribute("messaggio", "Inserimento non effettuato a causa di un errore!");
			return false;
		}

	}

    /**
	 * Inserisci nuovo Utente
	 *
	 * @param request contiene i parametri del nuovo utente
	 * @return boolean che indica il successo dell'azione dell'INSERT
	 */

	private boolean InsertUtente(HttpServletRequest request) {
		if (Utente.inserisciUtente(
				request.getParameter("email").toString(),
				request.getParameter("nome").toString(),
				request.getParameter("cognome").toString(),
				request.getParameter("password").toString(),
				request.getParameter("privilegi").toString().charAt(0), 
				request.getParameter("telefono").toString())) {
			// Inserimento effettuato con successo
			request.getSession().setAttribute("messaggio", "Inserimento effettuato con successo!");
			return true;
		} else {
			// Inserimento non andato a buon fine
			request.getSession().setAttribute("messaggio", "Inserimento non effettuato a causa di un errore!");
			return false;
		}
	}

    /**
	 * Controlla che la ripetizione della password inserita sia corretta
	 *
	 * @param request contiene i parametri del nuovo utente
	 * @return boolean che indica il successo dei controlli
	 */

	private boolean controllaPassword(HttpServletRequest request) {
		if (!request.getParameter("password").equals(request.getParameter("passwordR"))) {
			// Passoword non corrispondono
			request.getSession().setAttribute("messaggio", "Le password non corrispondono!");
			return false;
		}
		return true;
	}

    /**
	 * Cerca l'utente nel sistema
	 *
	 * @param request contiene i parametri del nuovo utente
	 * @return boolean che indica se esiste(false) o no(true) l'utente
	 */

	private boolean trovaUtente(HttpServletRequest request) {
		if (Utente.trovaUtente(request.getParameter("email").toString())) {
			// Utente esiste già
			request.getSession().setAttribute("messaggio", "Utente con email "+request.getParameter("email")+" già esistente!");
			return false;
		}
		return true;

	}

    /**
	 * Controlla che i dati inseriti siano corretti
	 *
	 * @param request contiene i parametri del nuovo utente
	 * @return boolean che indica il successo dei controlli
	 */

	private boolean controllaDati(HttpServletRequest request) {
		String messaggio = "";
		// Controllo dati
		// Se uno fallisce ret false
		String email = request.getSession().getAttribute("azione").toString()
				.equalsIgnoreCase("Nuovo Utente") ? request.getParameter(
				"email").toString() : request.getSession().getAttribute("utenteSel").toString();
			
		// nel sistema ci deve essere almeno un amministratore 
		int count = 0;
		List<Utente> listaUtenti = Utente.getUsers();
		if ( request.getParameter("privilegi").toString().charAt(0) != 'A' ){
			for ( Utente u : listaUtenti ){
				if ( u.getPrivilegi() == 'A' && (!u.getEmail().equals(email)) )
					count ++;
			}
			if ( count == 0 )
				messaggio += "Deve essere presente almeno un amministratore!</br>";
		}

		// La password eve contenere almeno 6 caratteri
		if (request.getParameter("password").toString().length() < 5)
			messaggio += "La password deve contenere almeno 6 caratteri </br>";

		// Gestore deve essere compreso tra 0 e 2
		char value = request.getParameter("privilegi").toString().charAt(0);

		if (value != utente && value != admin)
			messaggio += "Il Livello di Privilegi deve essere A oppure U </br>";

		if (!messaggio.isEmpty()) {
			request.getSession().setAttribute("messaggio", messaggio);
			return false;
		}
		return true;
	}
}
