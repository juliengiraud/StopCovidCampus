package fr.univlyon1.m1if.m1if03.servlets.controller;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(name = "presence", urlPatterns = "/presence")
public class Presence extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        initAttributes(request);
        getServletContext().getRequestDispatcher("/interface.jsp").include(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getParameter("login") == null) { // Traitement du formulaire envoyé par saisie_passage.jsp
            saisiePassage(request);
        }

        initAttributes(request);
        getServletContext().getRequestDispatcher("/interface.jsp").include(request, response);
    }

    private void saisiePassage(HttpServletRequest request) {
        String nomSalle = request.getParameter("nom");
        Salle salle;
        Map<String, Salle> salles = (Map<String, Salle>) getServletContext().getAttribute("salles");
        if (salles.get(nomSalle) == null) {
            salle = new Salle(nomSalle);
            salles.put(nomSalle, salle);
        } else
            salle = salles.get(nomSalle);
        User user = (User) request.getSession().getAttribute("user");

        if (request.getParameter("entree") != null) {
            Passage p = new Passage(user, salle, new Date());
            ((GestionPassages) getServletContext().getAttribute("passages")).add(p);
            salle.incPresent();
        } else if (request.getParameter("sortie") != null) {
            List<Passage> passTemp = ((GestionPassages) getServletContext().getAttribute("passages")).getPassagesByUserAndSalle(user, salle);
            for (Passage p : passTemp) { // On mémorise une sortie de tous les passages existants et sans sortie
                if (p.getSortie() == null) {
                    p.setSortie(new Date());
                    salle.decPresent();
                }
            }
        }
    }

    private void initAttributes(HttpServletRequest request) {
        request.setAttribute("passages", getServletContext().getAttribute("passages"));
        request.setAttribute("salles", getServletContext().getAttribute("salles"));
        request.setAttribute("users", getServletContext().getAttribute("users"));
    }

}
