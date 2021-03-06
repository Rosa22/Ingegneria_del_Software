package univr.is.tmc.servlet;

import univr.is.tmc.entity.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VeicoliUtenteServlet extends HttpServlet {

    /**
	 * VeicoliUtenteServlet, 
     * IF (azione = "Nuova Associazione")
     *      vai a modificaVeicoloUtente.jsp
     * IF (azione = "Modifica")
     *      set Attribute veicoloUtenteSel
     *      vai a modificaVeicoloUtente.jsp
     * IF (azione = "Elimina")
     *      elimina associazione
     * vai a modificaEffettuata.jsp
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String azione = request.getParameter("azione");
		String veicoloUtenteSel = request.getParameter("veicoloUtenteSel");
		// setAttribute di azione
		request.getSession().setAttribute("azione", azione);

		if (azione.equalsIgnoreCase("Nuova Associazione"))
			response.sendRedirect("modificaVeicoloUtente.jsp");

		if (azione.equalsIgnoreCase("Modifica")) {
            // setAttribute di veicoloUtenteSel
			request.getSession().setAttribute("veicoloUtenteSel", veicoloUtenteSel);
			response.sendRedirect("modificaVeicoloUtente.jsp");
		}
		if (azione.equalsIgnoreCase("Elimina")) {

			String[] items = veicoloUtenteSel.split(",");
			String targa = items[0];
			String email = items[1];
            // Elimina
			if (VeicoloUtente.eliminaVeicoloUtente(targa, email)) {
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
