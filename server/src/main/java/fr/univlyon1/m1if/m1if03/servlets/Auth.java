package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Auth", urlPatterns = {"/*"})
public class Auth extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getRequestURI().contains("/static/")) {
            chain.doFilter(req, res);
            return;
        }
        if (!req.getServletPath().substring(1).contains("index.jsp")) {
            if (req.getSession().getAttribute("user") != null) {
                chain.doFilter(req, res);
            } else if (req.getParameter("login") != null && req.getParameter("login") != "") {
                User user = new User(req.getParameter("login"));
                user.setNom(req.getParameter("nom"));
                user.setAdmin(req.getParameter("admin") != null && req.getParameter("admin").equals("on"));
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                chain.doFilter(req, res);
            } else {
                getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp").include(req, res);
            }
        } else {
            chain.doFilter(req, res);
        }
    }
}
