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

        if (request.getRequestURI().contains("/passages") || request.getRequestURI().contains("/salles")) {
            if (session == null || userSession == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous n'êtes pas connecté."); // 401
                return;
            }
            chain.doFilter(request, response);
            return;
        } else if (request.getRequestURI().contains("/users")) {
            if (!(request.getMethod().equals("POST") && request.getRequestURI().contains("users/login")) && (session == null || userSession == null)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous n'êtes pas connecté."); // 401
                return;
            }
            chain.doFilter(request, response);
            return;
        }
    }
}