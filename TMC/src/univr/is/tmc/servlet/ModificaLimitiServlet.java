package univr.is.tmc.servlet;

import univr.is.tmc.entity.Veicolo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModificaLimitiServlet extends HttpServlet {

    /**
	 * ModificaLimitiServlet, 
     * IF (azione = "Modifica")
     *      IF ( controllo dati = OK )
     *      applica modifica
     * vai a modificaEffettuata.jsp
	 */
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String azione = request.getSession().getAttribute("azione").toString();

		if (azione.equalsIgnoreCase("Modifica")) {
			// I dati nella form sono corretti?
			if (controllaDati(request))
				// Password corrispondono?
				this.UpdateLimite(request);
		}

		response.sendRedirect("modificaEffettuata.jsp");
	}

    /**
	 * Aggiorna Limite relativo ad un Veicolo
	 *
	 * @param request contiene il veicolo in questione e il nuovo limite
	 * @return boolean che indica il successo dell'azione dell' UPDATE
	 */

	private boolean UpdateLimite(HttpServletRequest request) {
		if (Veicolo.impostaLimite(
				request.getSession().getAttribute("carLimSel").toString(),
				Integer.parseInt(request.getParameter("limite").toString()) )) {
			// Modifica effettuata con successo
			request.getSession().setAttribute("messaggio", "Modifica effettuata con successo!");
			return true;
		} else {
			// Inserimento non andato a buon fine
			request.getSession().setAttribute("messaggio", "Inserimento non effettuato a causa di un errore!");
			return false;
		}

	}

    /**
	 * Controlla che i campi ricevuti in request siano corretti
	 *
	 * @param request contiene il nuovo limite
	 * @return boolean che indica il successo dei controlli eseguiti
	 */

	private boolean controllaDati(HttpServletRequest request) {
		String messaggio = "";
		// Controllo dati
		// Se uno fallisce return false
		int limite = Integer.parseInt(request.getParameter("limite").toString());
		if ( limite < 0 )
			messaggio += "Il limite deve essere maggiore di 0 </br>";

		if ( limite > 300 )
			messaggio += "Il limite deve essere minore di 300 </br>";

		if (!messaggio.isEmpty()) {
			request.getSession().setAttribute("messaggio", messaggio);
			return false;
		}
		return true;
	}
}
