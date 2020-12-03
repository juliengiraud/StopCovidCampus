package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.utils.Utilities;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "NegociationContenu")
public class NegociationContenu extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        String prefixe = getServletContext().getInitParameter("prefixe");
        String sufffixe = getServletContext().getInitParameter("suffixe");
        String accept = request.getHeader("Accept");
        request.setAttribute("accept", Utilities.getAcceptType(request));
        request.getRequestDispatcher(prefixe + request.getAttribute("viewPath") + sufffixe).include(request, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
