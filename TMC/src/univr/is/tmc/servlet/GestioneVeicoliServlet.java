package univr.is.tmc.servlet;

import univr.is.tmc.entity.Veicolo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneVeicoliServlet extends HttpServlet {

    /**
	 * GestioneVeicoliServlet, 
     * IF (azione = "Nuovo Veicolo")
	 *   vai a modificaVeicolo.jsp
     *
     * IF (azione = "Modifica")
     *   set Attribute "veicoloSel"
	 *   vai a modificaVeicolo.jsp
	 * 
     * IF (azione = "Elimina")
     *   elimino utente veicoloSel
	 *   vai a modificaEffettuata.jsp
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String azione = request.getParameter("azione");
		String veicoloSel = request.getParameter("veicoloSel");
		// setAttribute di azione
		request.getSession().setAttribute("azione", azione);

		if (azione.equalsIgnoreCase("Nuovo Veicolo"))
			response.sendRedirect("modificaVeicolo.jsp");

		if (azione.equalsIgnoreCase("Modifica")){
            // setAttribute di veicoloSel
			request.getSession().setAttribute("veicoloSel", veicoloSel);
			response.sendRedirect("modificaVeicolo.jsp");
        }
		if (azione.equalsIgnoreCase("Elimina")) {
			// Elimina
			if (Veicolo.eliminaVeicolo(veicoloSel)) {
				// Eliminazione andata a buon fine
				request.getSession().setAttribute("messaggio", "Eliminazione effettuata con successo!");
			} else {
				// Eliminazione non andata a buon fine
				request.getSession().setAttribute("messaggio", "Errore!");
			}
			response.sendRedirect("modificaEffettuata.jsp");
		}
	}
}
