package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebFilter(filterName = "CacheAccueil", urlPatterns = {"/presence"})
public class CacheAccueil extends HttpFilter {
    private User user;
    private GestionPassages gestionPassages;
    private HashMap<String, Salle> salles;
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        this.user = (User) req.getSession().getAttribute("user");
        this.gestionPassages = (GestionPassages) req.getServletContext().getAttribute("passages");
        this.salles =  (HashMap<String, Salle>) req.getServletContext().getAttribute("salles");

        if (req.getMethod().equals("GET")) {
            String eTagFromBrowser = req.getHeader("If-None-Match");
            String eTagFromServer = this.getTag();
            if (eTagFromBrowser != null && eTagFromBrowser.equals(eTagFromServer)) {
                res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            } else {
                res.setHeader("ETag", eTagFromServer);
            }
            chain.doFilter(req, res);
        } else {
            chain.doFilter(req, res);
        }
    }

    private String getTag() {
        //Liste de salles sans doublons
        Set<Salle> salleSet = new HashSet<>();
        String eTag = "\"";
        for (Passage p : this.gestionPassages.getPassagesByUser(this.user)) {
            if (!salleSet.contains(p.getSalle())) {
                eTag += "(" + p.getSalle().getNom() + ";" + p.getSalle().getSaturee() + "),";
                salleSet.add(p.getSalle());
            }
        }
        //Suppression de la dernière virgule de la chaine
        if (eTag.length() > 0) {
            eTag = eTag.substring(0, eTag.length()-1);
        }
        eTag += "\"";
        return eTag;
    }
}
