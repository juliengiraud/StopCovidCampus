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
        User userSession = (User) req.getSession().getAttribute("user");
        List<Route> whiteListedPaths = Route.getWhiteList(userSession);
        String path = req.getRequestURI();

        for (Route route : whiteListedPaths) {
            if (path.contains(route.getPath()) && req.getMethod().equals(route.getMethod())) {
                chain.doFilter(req, resp);
                return;
            }
        }

        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas administrateur.");
    }
}