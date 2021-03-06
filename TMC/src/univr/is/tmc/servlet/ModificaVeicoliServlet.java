package univr.is.tmc.servlet;

import univr.is.tmc.entity.Veicolo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModificaVeicoliServlet extends HttpServlet {

    /**
	 * ModificaVeicoliServlet, 
     * IF (azione = "Nuovo Veicolo")
     *      IF ( controllo dati = OK )
     *          IF ( veicolo esiste = NO )
     *              inserisci veicolo
     * IF (azione = "Modifica")
     *      IF ( controllo dati = OK )
     *          modifica veicolo
     * vai a modificaEffettuata.jsp
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String azione = request.getSession().getAttribute("azione").toString();

		if (azione.equalsIgnoreCase("Nuovo Veicolo")) {
			// I dati nella form sono corretti?
			if (controllaDati(request))
				// Esiste già?
				if (this.trovaVeicolo(request))
					// Password corrispondono?
					this.InsertVeicolo(request);
		}
		if (azione.equalsIgnoreCase("Modifica")) {
			// I dati nella form sono corretti?
			if (controllaDati(request))
				// Password corrispondono?
				this.UpdateVeicolo(request);
		}

		response.sendRedirect("modificaEffettuata.jsp");
	}

    /**
	 * Aggiorna Veicolo
	 *
	 * @param request contiene i nuovi parametri del veicolo
	 * @return boolean che indica il successo dell'azione dell'UPDATE
	 */

	private boolean UpdateVeicolo(HttpServletRequest request) {
		if (Veicolo.modificaVeicolo(
				request.getSession().getAttribute("veicoloSel").toString(),
				request.getParameter("marca").toString(),
				request.getParameter("modello").toString())) {
			// Modifica effettuata con successo
			request.getSession().setAttribute("messaggio", "Modifica effettuata con successo!");
			return true;
		} else {
			// Modifica non andata a buon fine
			request.getSession().setAttribute("messaggio", "Inserimento non effettuato a causa di un errore!");
			return false;
		}

	}

    /**
	 * Inserisce un nuovo Veicolo
	 *
	 * @param request contiene i parametri del nuovo veicolo
	 * @return boolean che indica il successo dell'azione dell'INSERT
	 */

	private boolean InsertVeicolo(HttpServletRequest request) {
		if (Veicolo.inserisciVeicolo(
				request.getParameter("targa").toString(),
				request.getParameter("marca").toString(),
				request.getParameter("modello").toString())) {
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
	 * Cerca se e' presente il veicolo in questione
	 *
	 * @param request contiene i parametri del veicolo
	 * @return boolean che indica se esiste(false) o no(true) il veicolo
	 */

	private boolean trovaVeicolo(HttpServletRequest request) {
		if (Veicolo.trovaVeicolo(request.getParameter("targa").toString())) {
			// Utente esiste già
			request.getSession().setAttribute("messaggio", "Veicolo targato "+request.getParameter("targa")+" già esistente!");
			return false;
		}
		return true;
	}

    /**
	 * Controlla che i dati inseriti siano corretti
	 *
	 * @param request contiene i parametri del nuovo veicolo
	 * @return boolean che indica il successo dei controlli
	 */

	private boolean controllaDati(HttpServletRequest request) {
		String messaggio = "";
		// Controllo dati
		// Se uno fallisce return false

		if (request.getParameter("marca").toString().length() > 20)
			messaggio += "La marca deve contenere meno di 20 caratteri </br>";

		if (request.getParameter("modello").toString().length() > 20)
			messaggio += "Il modello deve contenere meno di 20 caratteri </br>";

		if (!messaggio.isEmpty()) {
			request.getSession().setAttribute("messaggio", messaggio);
			return false;
		}
		return true;
	}
}
