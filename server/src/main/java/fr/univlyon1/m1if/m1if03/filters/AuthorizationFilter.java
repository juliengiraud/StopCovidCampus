package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Route;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.utils.PresenceUcblJwtHelper;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebFilter(filterName = "AuthorizationFilter")
public class AuthorizationFilter extends HttpFilter {

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = PresenceUcblJwtHelper.getTokenFromRequest(request);
        User user;
        String userLogin = (String) request.getAttribute("userLogin");
        List<Route> whiteListedPaths;
        if (userLogin != null) {
            user = new User(userLogin, PresenceUcblJwtHelper.verifyAdmin(token));
            GestionPassages passages = (GestionPassages) this.getServletContext().getAttribute("passages");
            Map<String, User> users = (Map<String, User>) this.getServletContext().getAttribute("users");;
            whiteListedPaths = Route.getWhiteList(user, passages.getPassagesByUser(users.get(userLogin)));
        } else {
            whiteListedPaths = Route.getWhiteList(null, null);
        }

        String path = request.getRequestURI();

        for (Route route : whiteListedPaths) {
            if (path.contains(route.getPath()) && request.getMethod().equals(route.getMethod())) {
                chain.doFilter(request, response);
                return;
            }
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur non administrateur");
    }
}