package univr.is.tmc.servlet;

import univr.is.tmc.entity.PosizioneFurto;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TravelServlet extends HttpServlet {

    /**
	 * TravelServlet, utilizzata dal JavaScript StoricoFurti e LiveTracking
     * trova tutte le posizioni dall'inizio, del furto scelto, fino ad ora
     * inserisco le posizioni in un Map e inglobo in un jSon
	 */
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

		ArrayList ar = new ArrayList();

		List<PosizioneFurto> posVeicolo = this.trovaPosizioniVeicolo(request);

		for (PosizioneFurto p : posVeicolo) {
			Map<String, String> options = new LinkedHashMap<String, String>();
			options.put("lat", p.getLatitudine());
			options.put("lng", p.getLongitudine());
			options.put("ora", p.getOra());
			ar.add(options);
		}

		String json = new Gson().toJson(ar); 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
		out.close();

	}

    /**
	 * TravelServlet, doPost utilizzato come redirect tra StoricoFurti e LiveTracking
     * IF (azione = "Live Tracking")
     *      set Attribute furtoSel
     *      vai a liveTracking.jsp
     * IF (azione = "Guarda Percorso")
     *      set Attribute furtoSel
     *      vai a furtoSel.jsp
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		String azione = request.getParameter("azione");
		String furtoSel = request.getParameter("furtoSel");
		// setAttribute di azione
		request.getSession().setAttribute("azione", azione);

		if (azione.equalsIgnoreCase("Live Tracking")) {
            // setAttribute di furtoSel
			request.getSession().setAttribute("furtoSel", furtoSel);
			response.sendRedirect("liveTracking.jsp");
		}
		if (azione.equalsIgnoreCase("Guarda Percorso")) {
            // setAttribute di furtoSel
			request.getSession().setAttribute("furtoSel", furtoSel);
			response.sendRedirect("furtoSel.jsp");
		}
	}
	
    /**
	 * Cerca le Posizioni del Furto dal suo inizio
	 *
	 * @param request contiene l'id del furto in questione
	 * @return List PosizioneFurto con tutte le posizioni del ladro fino ad ora
	 */

	private List<PosizioneFurto> trovaPosizioniVeicolo(HttpServletRequest request)
	{
		int idFurto = Integer.parseInt(request.getParameter("idFurto").toString());
		List<PosizioneFurto> posFurto = PosizioneFurto.getTravel( idFurto );

		if (posFurto.isEmpty())
			// Nessun veicolo trovato
			request.getSession().setAttribute("messaggio", "Nessuna posizione del furto "+idFurto+" trovata!<br/>");
		
		return posFurto;
	}

	
	
}
