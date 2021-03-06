package univr.is.tmc.servlet;

import univr.is.tmc.entity.Utente;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    /**
	 * LoginServlet, 
     * if ( controllo credenziali = ok )
     *      set Attribute currUserEmail
     *      set Attribute currUserMode
     *      vai a welcomeUser.jsp
     * else
     *      vai a invalidLogin.jsp
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {
			Utente user = Utente.getUserData(request.getParameter("user"));

			if (user != null && user.getPassword().equals(request.getParameter("pwd"))) {

				HttpSession session = request.getSession(true);
				session.setAttribute("currUserEmail", user.getEmail());
				session.setAttribute("currUserMode", user.getPrivilegi());

				// se MaxInactive Time a 30 min
				session.setMaxInactiveInterval(30 * 60);

				response.sendRedirect("welcomeUser.jsp");

			}else
				response.sendRedirect("invalidLogin.jsp");
		}
		catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}
