package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "SalleController", urlPatterns = "/salles/*")
public class SalleController extends HttpServlet {

    GestionPassages passages;
    Map<String, Salle> salles;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.passages = (GestionPassages) config.getServletContext().getAttribute("passages");
        this.salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 1) { // GET /salles
            doGetSalles(request, response);
        } else if (path.size() == 2) { // GET /salles/{salleId}
            doGetSalle(request, response, path.get(1));
        } else if (path.size() == 3 && path.get(2) == "passages"){ // GET /salles/{salleId}/passages
            doGetPassagesBySalle(request, response, path.get(1));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 1) { // POST /salles
            String nomSalle = request.getParameter("nomSalle");
            doCreateSalle(request, response, nomSalle);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 2) { // PUT /salles/{salleId}
            String salleId = request.getParameter("nomSalle");
            String capacite = request.getParameter("capacite");
            doUpdateSalle(request, response, salleId, capacite);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 1) { // DELETE /salles
            String nomSalle = request.getParameter("nomSalle");
            doDeleteSalle(request, response, nomSalle);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // GET /salles
    private void doGetSalles(HttpServletRequest request, HttpServletResponse response) {
        List<Salle> salle = new ArrayList<>(salles.values());
        // TODO envoyer salle
    }

    // GET /salles/{salleId}
    private void doGetSalle(HttpServletRequest request, HttpServletResponse response, String salleId) throws IOException {
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        // TODO envoyer salle
    }

    // GET /salles/{salleId}/passages
    private void doGetPassagesBySalle(HttpServletRequest request, HttpServletResponse response, String salleId) throws IOException {
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        List<Passage> passagesBySalle = passages.getPassagesBySalle(salle);
        // TODO envoyer passagesBySalle
    }

    // POST /salles
    private void doCreateSalle(HttpServletRequest request, HttpServletResponse response, String nomSalle) {
        Salle salle = new Salle(nomSalle);
        salles.put(nomSalle, salle);
    }

    // PUT /salles/{salleId}
    private void doUpdateSalle(HttpServletRequest request, HttpServletResponse response, String salleId, String capacite) throws IOException {
        int newCapacite;
        try {
            newCapacite = Integer.parseInt(capacite);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La capacité d'une salle doit être un nombre entier.");
            return;
        }
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        salle.setCapacite(newCapacite);
    }

    // DELETE /salles/{salleId}
    private void doDeleteSalle(HttpServletRequest request, HttpServletResponse response, String salleId) throws IOException {
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        salles.remove(salle);
    }
}
