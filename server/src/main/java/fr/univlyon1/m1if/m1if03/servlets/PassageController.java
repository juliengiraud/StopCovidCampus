package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.utils.Utilities;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "PassageController", urlPatterns = "/passages/*")
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("passages");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /passages

        if (path.size() == 1) { // GET /passages
            getPassages(request, response);
        } else if (path.size() == 2) { // GET /passages/{passageId}
            getPassage(request, response, path.get(1));
        } else if (path.size() == 3 && path.get(1).equals("byUser")) { // GET /passages/byUser/{userId}
            getPassagesByUser(request, response, path.get(2));
        } else if (path.size() == 4 && path.get(1).equals("byUser") && path.get(3).equals("enCours")) { // GET /passages/byUser/{userId}/enCours
            getPassagesByUserEnCours(request, response, path.get(2));
        } else if (path.size() == 5 && path.get(1).equals("byUserAndDates")) { // GET /passages/byUserAndDates/{userId}/{dateEntree}/{dateSortie}
            getPassagesByUserAndDates(request, response, path.get(2), path.get(3), path.get(4));
        } else if (path.size() == 3 && path.get(1).equals("bySalle")) { // GET /passages/bySalle/{salleId}
            getPassagesBySalle(request, response, path.get(2));
        } else if (path.size() == 5 && path.get(1).equals("bySalleAndDates")) { // GET /passages/bySalleAndDates/{salleId}/{dateEntree}/{dateSortie}
            getPassagesBySalleAndDates(request, response, path.get(2), path.get(3), path.get(4));
        } else if (path.size() == 4 && path.get(1).equals("byUserAndSalle")) { // GET /passages/byUserAndSalle/{userId}/{salleId}
            getPassagesByUserAndSalle(request, response, path.get(2), path.get(3));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("passages");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /salles

        if (path.size() == 1) { // POST /passages
            JSONObject data = new JSONObject(Utilities.getBody(request));
            String userId = "";
            String salleId = "";
            String dateEntree = "";
            String dateSortie = "";
            try {
                userId = data.getString("user");
                salleId = data.getString("salle");
                dateEntree = data.getString("dateEntree");
                dateSortie = data.getString("dateSortie");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Les paramètres ne sont pas acceptables"); //400
                return;
            }
            createPassage(request, response, userId, salleId, dateEntree, dateSortie);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
        int id;
        try {
            id = Integer.parseInt(passageId);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La capacité d'une salle doit être un nombre entier.");
            return;
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
        } // "mon nov 11 13:27:03 UTC 2020"
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
    private void createPassage(HttpServletRequest request, HttpServletResponse response, String userId,
                               String salleId, String dateEntree, String dateSortie) throws IOException {
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
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));

        Date entree = null;
        Date sortie = null;
        try {
            entree = dateEntree != null ? sdf.parse(dateEntree) : null;
            sortie = dateSortie != null ? sdf.parse(dateSortie) : null;
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La date est invalide.");
            return;
        }

        if (entree != null && sortie != null) {
            Passage p = new Passage(user, salle, new Date());
            passages.add(p);
            salle.incPresent();
        } else if (entree == null && sortie != null) {
            List<Passage> passTemp = passages.getPassagesByUserAndSalle(user, salle);
            for (Passage p : passTemp) { // On mémorise une sortie de tous les passages existants et sans sortie
                if (p.getSortie() == null) {
                    p.setSortie(new Date());
                    salle.decPresent();
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action invalide.");
            return;
        }
    }

}
