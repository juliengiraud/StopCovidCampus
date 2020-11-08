package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AuthAdmin", urlPatterns = {"/admin"})
public class AuthAdmin extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        User u = (User) req.getSession().getAttribute("user");
        if (u.getAdmin()) {
            chain.doFilter(req, res);
        } else {
            res.sendRedirect("presence");
        }
    }
}
