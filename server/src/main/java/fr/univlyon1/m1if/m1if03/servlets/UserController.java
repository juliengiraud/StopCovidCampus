package fr.univlyon1.m1if.m1if03.servlets;

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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserController", urlPatterns = "/users/*")
public class UserController extends HttpServlet {
    Map<String, User> users;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("users");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /users

        if (path.size() == 2) {
            switch (path.get(1)) {
                case "login":
                    JSONObject data = new JSONObject(Utilities.getBody(request));
                    String login = "";
                    String nom = "";
                    String admin = "";
                    try {
                        login = data.getString("login");
                        nom = data.getString("nom");
                        admin = data.getString("admin");
                    } catch (Exception e) {
                    }
                    if (!login.equals("")) {
                        User user = new User(login);
                        user.setNom(nom);
                        user.setAdmin(admin != null && admin.equals("on"));
                        request.getSession().setAttribute("user", user);
                        users.put(login, user);
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le login doit être renseigné.");
                        return;
                    }
                    break;

                case "logout":
                    User user = (User) request.getSession().getAttribute("user");
                    if (user == null) { // Non authentifié
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous n'est pas connecté."); //401
                        return;
                    }
                    request.getRequestDispatcher("Deco").forward(request, response);
                    break;
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("users");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /users


        if (path.size() == 1) { // GET /users
            getUsers(request, response);
        } else if (path.size() == 2) { // GET /users/{userId}
            String userId = path.get(1);
            getUserById(request, response, userId);
        } else if (path.size() == 3 && path.get(2).equals("passages")) { // GET /users/{userId}/passages
            String userId = path.get(1);
            response.sendRedirect("/passages/byUser/" + userId);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("users");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /users

        if (path.size() == 3 && path.get(2).equals("nom")) {
            String userId = path.get(1);
            JSONObject data = new JSONObject(Utilities.getBody(request));
            String userName = "";
            try{
                userName = data.getString("nom");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le nouveau nom n'est pas renseigné."); //400
                return;
            }
            updateUserName(request, response, userId, userName);
        }
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute("users", users);
        request.setAttribute("contenu", "users");
    }

    private void getUserById(HttpServletRequest request, HttpServletResponse response, String userId) throws IOException {
        User user = this.users.get(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + userId + " n'existe pas."); // 404
            return;
        }
        request.setAttribute("user", users.get(userId));
    }

    private void updateUserName(HttpServletRequest request, HttpServletResponse response, String userId, String userName) throws IOException {
        User user = this.users.get(userId);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + userId + " n'existe pas."); // 404
        }
        if (userName == null || userName.equals("")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le nouveau nom n'est pas renseigné."); //400
            return;
        }

        user.setNom(userName);
    }

}
