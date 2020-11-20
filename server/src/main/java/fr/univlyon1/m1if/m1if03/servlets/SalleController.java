package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Salle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "SalleController", urlPatterns = "/salle")
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

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (verifyRequest(request, response)) {
            Salle salle = salles.get(request.getParameter("nomSalle"));
            salles.remove(salle.getNom());
        }
        doGet(request, response);
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contenu = request.getParameter("contenu");
        if (contenu != null) {
            if (contenu.equals("salle")) {
                request.setAttribute("salle", salles.get(request.getParameter("nomSalle")));
            } else if (contenu.equals("salles")) {
                request.setAttribute("salles", salles);
            }
        }
        request.getRequestDispatcher("WEB-INF/jsp/interface_admin.jsp").include(request, response);
    }

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
}
