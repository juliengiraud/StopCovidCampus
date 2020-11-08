package fr.univlyon1.m1if.m1if03.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@WebFilter(filterName = "cache", urlPatterns = {"/presence", "/admin"})
public class Cache extends HttpFilter {
    //On stocke la date au premier chargement de la page
    private Date date = new Date();
    private int i = 1;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String param = req.getParameter("contenu");
        String url = req.getServletPath().substring(1);
        //Si la requête concerne les passages
        if (param != null && param.equals("passages")) {
            //S'il s'ajout d'un ajout de passage, on actualise la date
            if (url.equals("presence") && req.getMethod().equals("POST")) {
                this.date = new Date();
                chain.doFilter(req, res);
            } //S'il s'ajout de l'affichage de tous les passages par un admin
            else if (url.equals("admin") && req.getMethod().equals("GET")) {
                long lastModifiedFromBrowser = req.getDateHeader("If-Modified-Since")/1000*1000;
                long lastModifiedFromServer = this.date.getTime()/1000*1000;
                if (lastModifiedFromBrowser != -1 && lastModifiedFromServer <= lastModifiedFromBrowser) {
                    res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                } else {
                    res.setDateHeader("Last-Modified", this.date.getTime());
                    chain.doFilter(req, res);
                }
            } else {
                chain.doFilter(req, res);
            }
        } else {
            chain.doFilter(req, res);
        }
    }
}