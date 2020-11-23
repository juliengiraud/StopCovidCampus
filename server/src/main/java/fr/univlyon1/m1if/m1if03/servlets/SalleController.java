package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Salle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "SalleController", urlPatterns = "/salles/*") // TODO filtrer tous les non admins
public class SalleController extends HttpServlet {

    GestionPassages passages;
    Map<String, Salle> salles;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.passages = (GestionPassages) config.getServletContext().getAttribute("passages");
        this.salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 1) { // /salles
            doGetSalles(request, response);
        } else if (path.size() == 2) { // /salles/{salleId}
            doGetSalle(request, response, path.get(1));
        } else if (path.size() == 3 && path.get(2) == "passages"){ // /salles/{salleId}/passages
            doGetPassagesBySalle(request, response, path.get(1));
        } else {
            // 404
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 1) { // /salles
            doCreateSalle(request, response, request.getParameter("nomSalle"));
        } else {
            // 404
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 2) { // /salles/{salleId}
            try {
                var salleId = request.getParameter("nomSalle");
                var capacite = Integer.parseInt(request.getParameter("capacite"));
                doUpdateSalle(request, response, salleId, capacite);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La capacité d'une salle doit être un nombre entier.");
            }
        } else {
            // 404
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("salles");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 1) { // /salles
            doDeleteSalle(request, response, request.getParameter("nomSalle"));
        } else {
            // 404
        }
    }

    // TODO séparer ce truc moche en morceaux et les appeler aux bons endroits
    private boolean verifyRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("action") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action non spécifiée.");
            return false;
        }
        // Si le paramètre "action" est "Connexion", la requête est traitée dans le filtre, il n'y a rien à faire.
        if (request.getParameter("action").equals("connexion")) {
            return false;
        }

        String contenu = request.getParameter("contenu");
        if (contenu != null && contenu.equals("salles")) {
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

    // GET /salles
    private void doGetSalles(HttpServletRequest request, HttpServletResponse response) {
        var tmp = salles;
        // TODO envoyer tmp
    }

    // GET /salles/{salleId}
    private void doGetSalle(HttpServletRequest request, HttpServletResponse response, String salleId) {
        var tmp = salles.get(salleId);
        // TODO envoyer tmp
    }

    // GET /salles/{salleId}/passages
    private void doGetPassagesBySalle(HttpServletRequest request, HttpServletResponse response, String salleId) {
        var tmp = salles.get(salleId);
        var tmp2 = passages.getPassagesBySalle(tmp);
        // TODO envoyer tmp2
    }

    // POST /salles
    private void doCreateSalle(HttpServletRequest request, HttpServletResponse response, String nomSalle) {
        var tmp = new Salle(nomSalle);
        salles.put(nomSalle, tmp);
    }

    // PUT /salles/{salleId}
    private void doUpdateSalle(HttpServletRequest request, HttpServletResponse response, String salleId, int capacite) {
        var tmp = salles.get(salleId);
        tmp.setCapacite(capacite);
    }

    // DELETE /salles/{salleId}
    private void doDeleteSalle(HttpServletRequest request, HttpServletResponse response, String salleId) {
        var tmp = salles.get(salleId);
        salles.remove(tmp);
    }
}
