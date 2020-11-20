package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "UserController", urlPatterns = "/user")
public class UserController extends HttpServlet {
    Map<String, User> users;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String contenu = request.getParameter("contenu");
        if (contenu != null) {
            //GET sur un utilisateur particulier
            if (contenu.equals("user")) {
                if (request.getParameter("login") != null && !request.getParameter("login").equals(((User) (session.getAttribute("user"))).getLogin())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                } else if (session.getAttribute("user") == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                request.setAttribute("user", session.getAttribute("user"));
                request.getRequestDispatcher("WEB-INF/jsp/interface.jsp").include(request, response);
            }
            //GET sur la collection d'utilisateurs
            else if (contenu.equals("users")) {
                request.setAttribute("users", users);
                request.getRequestDispatcher("WEB-INF/jsp/interface_admin.jsp").include(request, response);
            }
        }
    }
}
