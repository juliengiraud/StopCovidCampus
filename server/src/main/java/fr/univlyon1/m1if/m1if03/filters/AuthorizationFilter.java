package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.Route;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "AuthorizationFilter")
public class AuthorizationFilter extends HttpFilter {
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        List<Route> whiteListedPaths = Route.getWhiteListForNonAdmin();
        String path = req.getRequestURI();
        User userSession = (User) req.getSession().getAttribute("user");

        if (userSession != null && userSession.getAdmin()) {
            chain.doFilter(req, resp);
        } else {
            // Exemple avec l'url tp4_war/users/login
            for (Route route : whiteListedPaths) {
                // l'url contient /users/login
                if (path.indexOf(route.getPath()) > 0) {
                    // On supprime tout ce qu'il y a avant /users/login dans l'url
                    path = path.substring(path.indexOf(route.getPath()));
                    // l'url est égale à users/login (il n'y a rien après et la méthode est bien POST
                    if (path.equals(route.getPath()) && req.getMethod().equals(route.getMethod())) {
                        // On poursuit
                        chain.doFilter(req, resp);
                        return;
                    }
                }
            }
            //todo gérer s'il faut verifier l'utilisateur connecté s'il n'est pas admin (ex pour avoir ses propres passages)
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas administrateur.");
        }
    }
}