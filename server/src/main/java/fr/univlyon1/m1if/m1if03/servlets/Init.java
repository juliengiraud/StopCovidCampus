package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Init", urlPatterns = "/Init")
public class Init extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        if (login == null && login.isEmpty()) {
            response.sendRedirect("index.html");
        }

        HttpSession session = request.getSession(true);
        Boolean admin = request.getParameter("admin") != null;
        session.setAttribute("user", new User(login));
        session.setAttribute("admin", admin);

        String nextJsp = admin ? "interface_admin.jsp" : "interface.jsp";
        request.getRequestDispatcher(nextJsp).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("index.html");
    }
}
