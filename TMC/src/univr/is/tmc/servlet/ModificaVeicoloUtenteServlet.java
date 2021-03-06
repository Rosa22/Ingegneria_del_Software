package univr.is.tmc.servlet;

import univr.is.tmc.entity.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModificaVeicoloUtenteServlet extends HttpServlet {

    /**
	 * ModificaVeicoloUtenteServlet, 
     * IF (azione = "Nuova Associazione")
     *      IF ( controllo dati = OK )
     *          IF ( utente esiste = OK )
     *              IF ( veicolo esiste = OK )
     *                  IF ( associazione esiste = NO )
     *                      inserisci associazione
     * IF (azione = "Modifica")
     *      IF ( controllo dati = OK )
     *          IF ( utente esiste = OK )
     *              IF ( veicolo esiste = OK )
     *                  IF ( associazione esiste = NO )
     *                      modifica associazione
     * vai a modificaEffettuata.jsp
	 */
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String azione = request.getSession().getAttribute("azione").toString();

		if (azione.equalsIgnoreCase("Nuova Associazione")) {
			// I dati nella form sono corretti?
			if (controllaDati(request))
				// Esiste già l'Utente?
				if (this.trovaUtente(request))
					// Esiste già il Veicolo?
					if (this.trovaVeicolo(request))
						// Esiste già l'associazione Utente->Veicolo?
						if (this.trovaVeicoloUtente(request))
							// Inserimento
							this.InsertVeicoloUtente(request);
		}
		if (azione.equalsIgnoreCase("Modifica")) {
			// I dati nella form sono corretti?
			if (controllaDati(request))
				// Esiste già l'Utente?
				if (this.trovaUtente(request))
					// Esiste già il Veicolo?
					if (this.trovaVeicolo(request))
						// Esiste già l'associazione Utente->Veicolo?
						if (this.trovaVeicoloUtente(request))
							// Inserimento
							this.UpdateVeicoloUtente(request);
		}

		response.sendRedirect("modificaEffettuata.jsp");
	}

    /**
	 * Aggiorna VeicoloUtente
	 *
	 * @param request contiene i nuovi parametri dell'Associazione
	 * @return boolean che indica il successo dell'azione dell'UPDATE
	 */

	private boolean UpdateVeicoloUtente(HttpServletRequest request) {

		String dati = request.getSession().getAttribute("veicoloUtenteSel").toString();
		String[] items = dati.split(",");
		String targa = items[0];
		String email = items[1];

		if (VeicoloUtente.modificaVeicoloUtente(targa, email)) {
			// Modifica effettuata con successo
			request.getSession().setAttribute("messaggio", "Modifica effettuata con successo!");
			return true;
		} else {
			// Modifica non andato a buon fine
			request.getSession().setAttribute("messaggio", "Inserimento non effettuato a causa di un errore!");
			return false;
		}

	}

    /**
	 * Inserisce un nuovo VeicoloUtente
	 *
	 * @param request contiene i parametri della nuova Associazione
	 * @return boolean che indica il successo dell'azione dell'INSERT
	 */

	private boolean InsertVeicoloUtente(HttpServletRequest request) {
		if (VeicoloUtente.inserisciVeicoloUtente(
				request.getParameter("targa").toString(),
				request.getParameter("email").toString())) {
			// Inserimento effettuato con successo
			request.getSession().setAttribute("messaggio", "Inserimento effettuato con successo!<br/>");
			return true;
		} else {
			// Inserimento non andato a buon fine
			request.getSession().setAttribute("messaggio", "Inserimento non effettuato a causa di un errore!<br/>");
			return false;
		}
	}

    /**
	 * Cerca se e' presente l'utente dell'associazione
	 *
	 * @param request contiene i parametri dell'associazione
	 * @return boolean che indica se esiste o no l'utente
	 */

	private boolean trovaUtente(HttpServletRequest request) {
		String email = request.getParameter("email").toString();
		if (Utente.trovaUtente(email)) {
			// OK! Utente esiste
			return true;
		}
		request.getSession().setAttribute("messaggio", "Utente con email "+request.getParameter("email").toString()+" non esiste!<br/>");
		return false;
	}

    /**
	 * Cerca se e' presente il veicolo dell'associazione
	 *
	 * @param request contiene i parametri dell'associazione
	 * @return boolean che indica se esiste o no il veicolo
	 */

	private boolean trovaVeicolo(HttpServletRequest request) {
		String targa = request.getParameter("targa").toString();
		if (Veicolo.trovaVeicolo(targa)) {
			// OK! Veicolo esiste
			return true;
		}
		request.getSession().setAttribute("messaggio", "Veicolo con targa "+targa+" non esiste!<br/>");
		return false;
	}

    /**
	 * Cerca se e' presente l'associazione in questione
	 *
	 * @param request contiene i parametri dell'associazione
	 * @return boolean che indica se esiste(false) o no(true) l'associazione
	 */

	private boolean trovaVeicoloUtente(HttpServletRequest request) {
		String targa = request.getParameter("targa").toString();
		String email = request.getParameter("email").toString();
		if (VeicoloUtente.trovaVeicoloUtente(targa, email) ) {
			// ERRORE! Associazione Veicolo-Utente esiste
			request.getSession().setAttribute("messaggio", "Associazione Veicolo -> Utente gia esistente!"+targa+" "+email+"<br/>");
			return false;
		}
		return true;
	}

    /**
	 * Controlla che i dati inseriti siano corretti
	 *
	 * @param request contiene i parametri della nuova associazione
	 * @return boolean che indica il successo dei controlli
	 */

	private boolean controllaDati(HttpServletRequest request) {
		String messaggio = "";
		// Controllo dati
		if (!messaggio.isEmpty()) {
			request.getSession().setAttribute("messaggio", messaggio);
			return false;
		}
		return true;
	}
}
