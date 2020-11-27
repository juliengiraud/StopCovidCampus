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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter extends HttpFilter {
    Map<String, User> users;

    @SuppressWarnings("unchecked")
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User userSession = (User) session.getAttribute("user");

        // Filtre de /passages
        if (request.getRequestURI().contains("/passages")) {
            if (session == null || userSession == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous n'êtes pas connecté."); // 401
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // Filtre de /salles
        if (request.getRequestURI().contains("/salles")) {
            if (session == null || userSession == null) { // Non authentifié
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous n'êtes pas connecté."); // 401
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // Filtre de /users
        if (request.getRequestURI().contains("/users")) {
            List<String> path = Arrays.asList(request.getRequestURI().split("/"));
            int startIndex = path.indexOf("users");
            int endIndex = path.size();
            path = path.subList(startIndex, endIndex);

            if (request.getRequestURI().contains("/users/login")) {
                chain.doFilter(request, response);
                return;
            } else if (session == null || userSession == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous n'êtes pas connecté."); // 401
                return;
            } else if (path.size() == 2 && users.get(path.get(1)) != null && users.get(path.get(1)).equals(userSession)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes connecté au bon compte."); // 403
                return;
            }
            chain.doFilter(request, response);
            return;
        }
    }
}