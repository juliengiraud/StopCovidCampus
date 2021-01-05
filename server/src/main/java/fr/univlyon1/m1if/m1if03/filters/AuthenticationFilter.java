package fr.univlyon1.m1if.m1if03.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import fr.univlyon1.m1if.m1if03.classes.Route;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.utils.PresenceUcblJwtHelper;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import java.io.IOException;
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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
        if (request.getMethod().equals(HttpMethod.OPTIONS)) {
            // response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            // response.setHeader("Access-Control-Allow-Credentials", "true");
            return;
        }

        List<Route> whiteListedPaths = Route.getWhiteList(null, null);
        String path = request.getRequestURI();
        String token = PresenceUcblJwtHelper.getTokenFromRequest(request);

        if (token != "") { // La requête contient un token
            try {
                String login = PresenceUcblJwtHelper.verifyToken(token, request);
                request.setAttribute("userLogin", login);
                chain.doFilter(request, response);
                return;
            } catch (NullPointerException | JWTVerificationException e) {
                response.sendRedirect("/" + request.getRequestURI().split("/")[1]);
                return;
            }
        }

        for (Route route : whiteListedPaths) {
            if (path.contains(route.getPath()) && request.getMethod().equals(route.getMethod())) {
                chain.doFilter(request, response);
                return;
            }
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utilisateur non authentifié");
    }
}