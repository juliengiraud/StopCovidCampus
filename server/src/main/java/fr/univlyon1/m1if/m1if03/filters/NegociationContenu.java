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

    private static String prefixe;
    private static String suffixe;

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        prefixe = config.getInitParameter("prefixe");
        suffixe = config.getInitParameter("suffixe");
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        response.setHeader("Access-Control-Allow-Origin", "/*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
        response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        if (!request.getMethod().equals(HttpMethod.GET) || response.getStatus() != 200 || request.getMethod().equals(HttpMethod.OPTIONS)) {
            return; // On a rien à faire ici
        }

        String accept = Utilities.getAcceptType(request);
        String contentType = "";
        String viewPath = (String) request.getAttribute("viewPath");

        if (accept.equals("json")) {
            contentType = "application/json";
        } else if (accept.equals("xml")) {
            contentType = "application/xml";
        } else {
            contentType = "text/html";
        }
        response.setHeader("Content-Type", contentType + ";charset=UTF-8");

        String jspPath = prefixe + accept + "/";
        jspPath += accept.equals("html") ? viewPath : accept;
        jspPath += suffixe;

        request.getRequestDispatcher(jspPath).include(request, response);
    }

}
