package fr.univlyon1.m1if.m1if03.servlets.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "admin", urlPatterns = "/admin")
public class Admin extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("passages", getServletContext().getAttribute("passages"));
        request.setAttribute("salles", getServletContext().getAttribute("salles"));
        request.setAttribute("users", getServletContext().getAttribute("users"));

        getServletContext().getRequestDispatcher("/interface_admin.jsp").include(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("passages", getServletContext().getAttribute("passages"));
        request.setAttribute("salles", getServletContext().getAttribute("salles"));
        request.setAttribute("users", getServletContext().getAttribute("users"));

        getServletContext().getRequestDispatcher("/interface_admin.jsp").include(request, response);
    }

}
