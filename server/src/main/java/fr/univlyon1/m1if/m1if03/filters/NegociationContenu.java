package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.utils.Utilities;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import java.io.IOException;

@WebFilter(filterName = "NegociationContenu")
public class NegociationContenu extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        if (!request.getMethod().equals(HttpMethod.GET)) {
            return; // On a rien à faire ici
        }

        String prefixe = getServletContext().getInitParameter("prefixe");
        String sufffixe = getServletContext().getInitParameter("suffixe");
        String accept = Utilities.getAcceptType(request);
        String contentType = "";
        String viewPath = (String) request.getAttribute("viewPath");

        if (accept.equals("json")) {
            contentType = "" + contentType;
        } else if (accept.equals("xml")) {
            contentType = "application/xml" + contentType;
        } else {
            contentType = "text/html" + contentType;
        }
        response.setHeader("Content-Type", contentType + ";charset=UTF-8");

        request.getRequestDispatcher(prefixe + viewPath + sufffixe).include(request, response);
    }

    public void init(FilterConfig config) {
    }

}
