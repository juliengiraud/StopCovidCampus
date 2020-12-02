package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.utils.Utilities;
import org.json.JSONException;
import org.json.JSONObject;

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 1) { // GET /salles
            doGetSalles(request, response);
        } else if (path.size() == 2) { // GET /salles/{salleId}
            doGetSalle(request, response, path.get(1));
        } else if (path.size() == 3 && path.get(2).equals("passages")) { // GET /salles/{salleId}/passages
            //tp4, tp4_war +...
            response.sendRedirect("/" + Arrays.asList(request.getRequestURI().split("/")).get(1) + "/passages/bySalle/" + path.get(1));
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
            JSONObject params;
            try {
                params = Utilities.getParams(request, Arrays.asList("nomSalle"));
            } catch (JSONException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String nomSalle = params.getString("nomSalle");
            if (nomSalle == null || nomSalle.equals("")) { // Paramètres non acceptables
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le nom de la salle n'est pas renseigné."); // 400
            }
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
            JSONObject params;
            try{
                params = Utilities.getParams(request, Arrays.asList("nomSalle", "capacite"));
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String salleId = params.getString("nomSalle");
            int capacite = -2;
            try {
                capacite = params.getInt("capacite");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Merci d'indiquer une capacité positive.");
                return;
            }
            if (salleId == null || salleId.equals("") || capacite < -1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Merci d'indiquer l'identifiant de la salle et sa capacité.");
            } else {
                doUpdateSalle(request, response, salleId, capacite);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 2) { // DELETE /salles/{salleId}
            String salleId = path.get(1);
            doDeleteSalle(request, response, salleId);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // GET /salles
    private void doGetSalles(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("WEB-INF/jsp/contenus/salles.jsp").include(request, response);
    }

    // GET /salles/{salleId}
    private void doGetSalle(HttpServletRequest request, HttpServletResponse response, String salleId) throws IOException, ServletException {
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        request.setAttribute("salle", salle);
        request.getRequestDispatcher("../WEB-INF/jsp/contenus/salle.jsp").include(request, response);
    }

    // POST /salles
    private void doCreateSalle(HttpServletRequest request, HttpServletResponse response, String nomSalle) {
        Salle salle = new Salle(nomSalle);
        salles.put(nomSalle, salle);
    }

    // PUT /salles/{salleId}
    private void doUpdateSalle(HttpServletRequest request, HttpServletResponse response, String salleId, int capacite) throws IOException {
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        salle.setCapacite(capacite);
    }

    // DELETE /salles/{salleId}
    private void doDeleteSalle(HttpServletRequest request, HttpServletResponse response, String salleId) throws IOException {
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        salles.remove(salle.getNom());
    }
}
