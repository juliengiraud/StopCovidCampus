package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.Routes;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "AuthorizationFilter")
public class AuthorizationFilter extends HttpFilter {
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        Map<String, String> whiteListedPaths = Routes.getWhiteList();

        for (String whiteListedPath : whiteListedPaths.keySet()) {
            String path = req.getRequestURI();
            if (path.contains(whiteListedPath)) {
                path = path.substring(path.indexOf(whiteListedPath));
                if (path.equals(whiteListedPath) && req.getMethod().equals(whiteListedPaths.get(whiteListedPath))) {
                    chain.doFilter(req, resp);
                    return;
                }
            }
        }

        if (((User) (req.getSession().getAttribute("user"))).getAdmin()) {
            chain.doFilter(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas administrateur.");
        }
    }
}