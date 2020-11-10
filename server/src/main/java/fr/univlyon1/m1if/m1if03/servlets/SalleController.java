package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "SalleController", urlPatterns = "/salles/*")
public class SalleController extends HttpServlet {
    Map<String, Salle> salles;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (verifyRequest(request, response)) {
            salles.put(request.getParameter("nomSalle"), new Salle(request.getParameter("nomSalle")));
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contenu = request.getParameter("contenu");
        if (contenu != null && contenu.equals("salle")) {
            request.setAttribute("salle", salles.get(request.getParameter("nomSalle")));
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/interface_admin.jsp");
        dispatcher.include(request, response);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (verifyRequest(request, response)) {
            Salle salle = salles.get(request.getParameter("nomSalle"));
            try {
                salle.setCapacite(Integer.parseInt(request.getParameter("capacite")));
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La capacité d'une salle doit être un nombre entier.");
                return;
            }
        }
        doGet(request, response);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (verifyRequest(request, response)) {
            Salle salle = salles.get(request.getParameter("nomSalle"));
            salles.remove(salle.getNom());
        }
        doGet(request, response);
    }

    private boolean verifyRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contenu = request.getParameter("contenu");
        if (contenu != null && contenu.equals("salle")) {
            Salle salle;
            if (request.getParameter("nomSalle") == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le nom de la salle doit être précisé.");
                return false;
            }
            salle = salles.get(request.getParameter("nomSalle"));
            if (salle == null
                    // On exclut le cas où on veut créer une nouvelle salle
                    && !(request.getParameter("action") != null && request.getParameter("action").equals("Ajouter"))) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + request.getParameter("nomSalle") + "n'existe pas.");
                return false;
            }
        }
        return true;
    }
}
