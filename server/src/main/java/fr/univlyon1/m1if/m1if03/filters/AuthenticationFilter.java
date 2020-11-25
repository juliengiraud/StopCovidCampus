package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter extends HttpFilter {
    Map<String, User> users;

    @SuppressWarnings("unchecked")
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        HttpSession session = request.getSession();
        User userSession = (User) session.getAttribute("user");

        // Filtre de /passages
        if (request.getRequestURI().contains("/passages")) {
            if (session == null || userSession == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401
            } else if (!request.getMethod().equals("POST") && !userSession.getAdmin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403
            }
            return;
        }

        // Filtre de /salles
        if (request.getRequestURI().contains("/passages")) {
            if (session == null || userSession == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401
            } else if (!userSession.getAdmin()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403
            }
            return;
        }

        // Test d'une requête d'authentification
        if (!request.getMethod().equals("POST")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (request.getParameter("action") != null && request.getParameter("action").equals("Connexion")
                && request.getParameter("login") != null && !request.getParameter("login").equals("")) {
            User user = new User(request.getParameter("login"));
            user.setNom(request.getParameter("nom"));
            user.setAdmin(request.getParameter("admin") != null && request.getParameter("admin").equals("on"));

            // On ajoute l'user à la session
            session = request.getSession(true);
            session.setAttribute("user", user);
            // On rajoute l'user dans le DAO

            users.put(request.getParameter("login"), user);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }

}