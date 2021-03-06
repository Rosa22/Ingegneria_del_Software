package univr.is.tmc.servlet;

import univr.is.tmc.entity.Utente;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllarmiServlet extends HttpServlet {
    
    /**
	 * Servlet Allarmi, 
     * IF (azione = "Vedi allarmi")
     *   set Attribute "veicoloSel"
	 *   vai a allarmiVeicolo.jsp
	 * 
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String azione = request.getParameter("azione");
		String veicoloSel = request.getParameter("veicoloSel");
		// setAttribute dei valori Utili
		request.getSession().setAttribute("azione", azione);
		request.getSession().setAttribute("veicoloSel", veicoloSel);

		if (azione.equalsIgnoreCase("Vedi Allarmi"))
			response.sendRedirect("allarmiVeicolo.jsp");
	}
}
