package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.RequestDispatcher;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "PassageController",  urlPatterns = "/passages/*")
public class PassageController extends HttpServlet {
    GestionPassages passages;
    Map<String, Salle> salles;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.passages = (GestionPassages) config.getServletContext().getAttribute("passages");
        this.salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (request.getParameter("action") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action non spécifiée.");
            return;
        }
        // Si le paramètre "action" est "Connexion", la requête est traitée dans le filtre, il n'y a rien à faire.
        if (!request.getParameter("action").equals("Connexion")) {
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

            if (request.getParameter("action").equals("Entrer")) {
                Passage p = new Passage(user, salle, new Date());
                passages.add(p);
                salle.incPresent();
            } else if (request.getParameter("action").equals("Sortir")) {
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
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contenu = request.getParameter("contenu");
        if (contenu != null) {
            if (contenu.equals("passage")) {
                if (request.getParameter("nomSalle") != null) {
                    if (request.getParameter("login") != null)
                        request.setAttribute("passagesAffiches", passages.getPassagesByUserAndSalle(new User(request.getParameter("login")), new Salle(request.getParameter("nomSalle"))));
                    else if (request.getParameter("dateEntree") != null && request.getParameter("dateSortie") != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                            Date dateEntree = sdf.parse(request.getParameter("dateEntree"));
                            Date dateSortie = sdf.parse(request.getParameter("dateSortie"));
                            request.setAttribute("passagesAffiches", passages.getPassagesBySalleAndDates(new Salle(request.getParameter("nomSalle")), dateEntree, dateSortie));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else
                        request.setAttribute("passagesAffiches", passages.getPassagesBySalle(new Salle(request.getParameter("nomSalle"))));
                } else if (request.getParameter("login") != null)
                    request.setAttribute("passagesAffiches", passages.getPassagesByUser(new User(request.getParameter("login"))));
                else
                    request.setAttribute("passagesAffiches", passages.getAllPassages());
            } else if (contenu.equals("passage")) {
                    try {
                        int id = Integer.parseInt(request.getParameter("num"));
                        request.setAttribute("passage", passages.getPassageById(id));
                    } catch(Exception e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "numéro du passage non présent ou invalide" + e.getLocalizedMessage());
                        return;
                    }
            }
        } else {
            // Attribut de requête pour default
            request.setAttribute("mesPassages", passages.getPassagesByUserEncours((User) request.getSession().getAttribute("user")));
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/interface_admin.jsp");
        dispatcher.include(request, response);
    }
}
