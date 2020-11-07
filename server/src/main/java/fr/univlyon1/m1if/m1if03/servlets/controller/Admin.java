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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "admin", urlPatterns = "/admin")
public class Admin extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        initAttributes(request);
        getServletContext().getRequestDispatcher("/interface_admin.jsp").include(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        initAttributes(request);

        if (request.getParameter("nomSalle") != null && request.getParameter("capacite") != null) {
            saisieCapaciteSalle(request);
        }
        getServletContext().getRequestDispatcher("/interface_admin.jsp").include(request, response);
    }

    private void saisieCapaciteSalle(HttpServletRequest request) {
        try {
            Map<String, Salle> salles = (Map<String, Salle>) getServletContext().getAttribute("salles");
            Salle salle = salles.get(request.getParameter("nomSalle"));
                salle.setCapacite(Integer.parseInt(request.getParameter("capacite")));
        } catch (NumberFormatException e) {
            request.setAttribute("errorCapaciteSalle", true);
        }
    }

    private void initPassagesAffiches(HttpServletRequest request) throws ParseException {
        // Get all parameters we need
        Salle salle = null;
        if (request.getParameter("nomSalle") != null) {
            salle = new Salle(request.getParameter("nomSalle"));
        }
        User user = null;
        if (request.getParameter("login") != null) {
            user = new User(request.getParameter("login"));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",
                new Locale("us"));
        Date dateEntree = null;
        if (request.getParameter("dateEntree") != null) {
            dateEntree = sdf.parse(request.getParameter("dateEntree"));
        }
        Date dateSortie = null;
        if (request.getParameter("dateSortie") != null) {
            dateSortie = sdf.parse(request.getParameter("dateSortie"));
        }

        // Call method with all parameters
        request.setAttribute("passagesAffiches", getPassageAffiches(salle, user,
                dateEntree, dateSortie));
    }

    private List<Passage> getPassageAffiches(Salle salle, User user, Date dateEntree, Date dateSortie) {
        GestionPassages passages = ((GestionPassages) getServletContext()
                .getAttribute("passages"));

        if (salle == null) {
            if (user != null) {
                passages.getPassagesByUser(user);
            }
            return passages.getAllPassages();
        }

        // From here salle != null
        if (user != null) {
            return passages.getPassagesByUserAndSalle(user, salle);
        }

        // From here user == null
        if (dateEntree != null && dateSortie != null) {
            return passages.getPassagesBySalleAndDates(salle, dateEntree, dateSortie);
        }
        return passages.getPassagesBySalle(salle);
    }

    private void initAttributes(HttpServletRequest request) {
        request.setAttribute("passages", getServletContext().getAttribute("passages"));
        request.setAttribute("salles", getServletContext().getAttribute("salles"));
        request.setAttribute("users", getServletContext().getAttribute("users"));
        request.setAttribute("errorCapaciteSalle", false);
        try {
            initPassagesAffiches(request);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
