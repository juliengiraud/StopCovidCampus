package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "PassageController", urlPatterns = "/passage/*") // TODO filtrer les 401
public class PassageController extends HttpServlet {

    GestionPassages passages;
    Map<String, Salle> salles;
    Map<String, User> users;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.passages = (GestionPassages) config.getServletContext().getAttribute("passages");
        this.salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        if (request.getParameter("action") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action non spécifiée.");
            return;
        }

        // Si le paramètre "action" est "Connexion", la requête est traitée dans le filtre, il n'y a rien à faire.
        if (action.equals("Connexion")) {
            doGet(request, response);
        }

        String nomSalle = request.getParameter("nomSalle");
        if (nomSalle == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Salle non spécifiée.");
            return;
        }
        Salle salle = salles.get(nomSalle);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + nomSalle + " n'existe pas.");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (action.equals("Entrer")) {
            Passage p = new Passage(user, salle, new Date());
            passages.add(p);
            salle.incPresent();
        } else if (action.equals("Sortir")) {
            List<Passage> passTemp = passages.getPassagesByUserAndSalle(user, salle);
            for (Passage p : passTemp) { // On mémorise une sortie de tous les passages existants et sans sortie
                if (p.getSortie() == null) {
                    p.setSortie(new Date());
                    salle.decPresent();
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action " + request.getParameter("action") + " invalide.");
            return;
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException { // TODO 403 pour tous les GET
        HttpSession session = request.getSession();
        User userSession = (User) session.getAttribute("user");
        String contenu = request.getParameter("contenu");

        // Attribut de requête par défaut
        if (contenu == null) {
            request.setAttribute("mesPassages", passages.getPassagesByUserEncours(userSession));
            response.setHeader("Refresh", "5");
            //request.getRequestDispatcher("WEB-INF/jsp/interface_admin.jsp").include(request, response);
            //return;
        }

        if (contenu.equals("passages")) {
            if (request.getParameter("nomSalle") != null) {
                if (request.getParameter("login") != null) {
                    request.setAttribute("passagesAffiches", passages.getPassagesByUserAndSalle(new User(request.getParameter("login")), new Salle(request.getParameter("nomSalle"))));
                } else if (request.getParameter("dateEntree") != null && request.getParameter("dateSortie") != null) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                        Date dateEntree = sdf.parse(request.getParameter("dateEntree"));
                        Date dateSortie = sdf.parse(request.getParameter("dateSortie"));
                        request.setAttribute("passagesAffiches", passages.getPassagesBySalleAndDates(new Salle(request.getParameter("nomSalle")), dateEntree, dateSortie));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    request.setAttribute("passagesAffiches", passages.getPassagesBySalle(new Salle(request.getParameter("nomSalle"))));
                }
                //IF NOT ADMIN request.setAttribute("passagesAffiches", passages.getPassagesByUserAndSalle(userSession, new Salle(request.getParameter("nomSalle"))));
            } else if (request.getParameter("login") != null) {
                    request.setAttribute("passagesAffiches", passages.getPassagesByUser(new User(request.getParameter("login"))));
            } else {
                request.setAttribute("passagesAffiches", passages.getAllPassages());
                //IF NOT ADMIN request.setAttribute("passagesAffiches", passages.getPassagesByUser(userSession));
            }
        } else if (contenu.equals("passage")) {
            try {
                int id = Integer.parseInt(request.getParameter("num"));
                Passage passage = passages.getPassageById(id);
                //IF NOT ADMIN
                /*if (!passage.getUser().equals(userSession)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }*/
                request.setAttribute("passage", passage);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "numéro du passage non présent ou invalide");
                return;
            }
        }
    }

    // GET /passages
    private void getPassages(HttpServletRequest request, HttpServletResponse response) {
        List<Passage> passage = passages.getAllPassages();
        // TODO envoyer passages
    }

    // GET /passages/{passageId}
    private void getPassage(HttpServletRequest request, HttpServletResponse response,
                            String passageId) throws IOException {
        int id = -1;
        try {
            id = Integer.parseInt(passageId);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La capacité d'une salle doit être un nombre entier.");
        }
        Passage passage = passages.getPassageById(id);
        if (passage == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le passage " + id + "n'existe pas.");
            return;
        }
        // TODO envoyer passage
    }

    // GET /passages/byUser/{userId}
    private void getPassagesByUser(HttpServletRequest request, HttpServletResponse response,
                                   String userId) throws IOException {
        User user = users.get(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + userId + "n'existe pas.");
            return;
        }
        List<Passage> passageByUser = this.passages.getPassagesByUser(user);
        // TODO envoyer passageByUser
    }

    // GET /passages/byUser/{userId}/enCours
    private void getPassagesByUserEnCours(HttpServletRequest request, HttpServletResponse response,
                                          String userId) throws IOException {
        User user = users.get(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + userId + "n'existe pas.");
            return;
        }
        List<Passage> passageByUserEnCours = this.passages.getPassagesByUserEncours(user);
        // TODO envoyer passageByUserEnCours
    }

    // GET /passages/byUserAndDates/{userId}/{dateEntree}/{dateSortie}
    private void getPassagesByUserAndDates(HttpServletRequest request, HttpServletResponse response,
                                          String userId, String dateEntree, String dateSortie) throws IOException {
        User user = users.get(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + userId + "n'existe pas.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
        Date entree;
        Date sortie;
        try {
            entree = sdf.parse(dateEntree);
            sortie = sdf.parse(dateSortie);
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La date est invalide.");
            return;
        }
        List<Passage> passagesByUserAndDates = passages.getPassagesByUserAndDates(user, entree, sortie);
        // TODO envoyer passagesByUserAndDates
    }

    // GET /passages/bySalle/{salleId}
    private void getPassagesBySalle(HttpServletRequest request, HttpServletResponse response,
                                    String salleId) throws IOException {
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        List<Passage> passagesBySalle = passages.getPassagesBySalle(salle);
        // TODO envoyer passagesBySalle
    }

    // GET /passages/bySalleAndDates/{salleId}/{dateEntree}/{dateSortie}
    private void getPassagesBySalleAndDates(HttpServletRequest request, HttpServletResponse response,
                                            String salleId, String dateEntree, String dateSortie) throws IOException {
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
        Date entree;
        Date sortie;
        try {
            entree = sdf.parse(dateEntree);
            sortie = sdf.parse(dateSortie);
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La date est invalide.");
            return;
        }
        List<Passage> passagesBySalleAndDates = passages.getPassagesBySalleAndDates(salle, entree, sortie);
        // TODO envoyer passagesBySalleAndDates
    }

    // GET /passages/byUserAndSalle/{userId}/{salleId}
    private void getPassagesByUserAndSalle(HttpServletRequest request, HttpServletResponse response,
                                           String userId, String salleId) throws IOException {
        User user = users.get(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + userId + "n'existe pas.");
            return;
        }
        Salle salle = salles.get(salleId);
        if (salle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + salleId + "n'existe pas.");
            return;
        }
        List<Passage> passagesByUserAndSalle = passages.getPassagesByUserAndSalle(user, salle);
        // TODO envoyer passagesByUserAndSalle
    }

    // POST /passages {"user": "string", salle": "string", dateEntree": "string", dateSortie": "string"}
    private void createPassage(HttpServletRequest request, HttpServletResponse response) {
        // TODO
    }

}
