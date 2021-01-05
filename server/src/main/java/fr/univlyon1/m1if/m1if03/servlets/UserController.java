package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.classes.dto.UserDTO;
import fr.univlyon1.m1if.m1if03.classes.dto.UsersDTO;
import fr.univlyon1.m1if.m1if03.utils.PresenceUcblJwtHelper;
import fr.univlyon1.m1if.m1if03.utils.Utilities;
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

@WebServlet(name = "UserController", urlPatterns = "/users/*")
public class UserController extends HttpServlet {
    Map<String, User> users;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("test 4");
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("users");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /users

        if (path.size() == 2) {
            switch (path.get(1)) {
                case "login":
                    JSONObject params;
                    try {
                        params = Utilities.getParams(request, Arrays.asList("login", "nom", "admin"));
                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables");
                        return;
                    }
                    String login = params.getString("login");
                    String nom = params.getString("nom");
                    Boolean admin = params.getBoolean("admin");
                    doLogin(request, response, login, nom, admin);
                    break;

                case "logout":
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
            //tp4, tp4_war +...
            response.setStatus(HttpServletResponse.SC_SEE_OTHER);
            response.sendRedirect("/" + request.getRequestURI().split("/")[1] + "/passages/byUser/" + path.get(1));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> path = Arrays.asList(request.getRequestURI().split("/"));
        int startIndex = path.indexOf("users");
        int endIndex = path.size();
        path = path.subList(startIndex, endIndex); // "path" commence à partir de /users

        if (path.size() == 3 && path.get(2).equals("nom")) {
            String userId = path.get(1);
            JSONObject params;
            try {
                params = Utilities.getParams(request, Arrays.asList("nom"));
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables"); //400
                return;
            }
            String userName = params.getString("nom");
            if (userName == "") {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables"); //400
            }
            updateUserName(request, response, userId, userName);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<User> users = new ArrayList<>(this.users.values());
        UsersDTO dto = new UsersDTO(Utilities.getPathBase(request));
        dto.setUsers(users);
        request.setAttribute("dto", dto);
        request.setAttribute("viewPath", "users");
    }

    private void getUserById(HttpServletRequest request, HttpServletResponse response, String userId) throws IOException, ServletException {
        User user = this.users.get(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur non trouvé"); // 404
            return;
        }
        UserDTO dto = new UserDTO(Utilities.getPathBase(request));
        dto.setUser(user);
        request.setAttribute("dto", dto);
        request.setAttribute("viewPath", "user");
    }

    private void updateUserName(HttpServletRequest request, HttpServletResponse response, String userId, String userName) throws IOException {
        User user = this.users.get(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur non trouvé"); // 404
            return;
        }
        user.setNom(userName);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.setHeader("Location", Utilities.getPathBase(request) + "/users/" + userId);
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response, String login, String nom, Boolean admin) throws IOException {
        System.out.println("test 5");
        if (login.equals("")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètres de la requête non acceptables");
            return;
        }
        User user = new User(login);
        user.setNom(nom);
        user.setAdmin(admin);
        String token = PresenceUcblJwtHelper.generateToken(login, admin, request);
        response.setHeader("Authorization", "Bearer: " + token);
        users.put(login, user);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.setHeader("Location", Utilities.getPathBase(request) + "/users/" + login);
    }
}
