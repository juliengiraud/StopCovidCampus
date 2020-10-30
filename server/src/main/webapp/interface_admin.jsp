<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Jérémy
  Date: 28/10/2020
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>

<% if (request.getSession().getAttribute("user") == null) {
    response.sendRedirect("index.html");
    return;
}%>

<c:if test="${!sessionScope.admin}">
    <c:redirect url = "interface.jsp"/>
</c:if>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Présence UCBL</title>
        <!-- CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
        <!-- jQuery and JS bundle w/ Popper.js -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
    </head>
    <body>
        <header>
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <a href="interface_admin.jsp" class="navbar-brand">Présence UCBL</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a href="interface_admin.jsp?page=passage" class="nav-link">Voir mes passages</a>
                        </li>
                        <li class="nav-item">
                            <a href="interface_admin.jsp?page=passages" class="nav-link">Voir tous les passages</a>
                        </li>
                        <li class="nav-item">
                            <a href="interface_admin.jsp?page=passagesByUser" class="nav-link">Voir tous les passages d'un utilisateur</a>
                        </li>
                        <li class="nav-item">
                            <a href="interface_admin.jsp?page=passagesBySalle" class="nav-link">Voir tous les passages d'une salle</a>
                        </li>
                        <li class="nav-item">
                            <a href="interface_admin.jsp?page=salles" class="nav-link">Voir toutes les salles</a>
                        </li>
                        <li class="nav-item">
                            <a href="interface.jsp?page=user_me" class="nav-link">Voir mon profil</a>
                        </li>
                        <li class="nav-item">
                            <a href="interface_admin.jsp?page=contacts" class="nav-link">Voir mes contacts</a>
                        </li>
                        <li class="nav-item">
                            <a href="interface_admin.jsp?page=user" class="nav-link">Voir un utilisateur</a>
                        </li>
                        <li class="nav-item">
                            <a href="saisie.html" class="nav-link">Saisir un nouveau passage</a>
                        </li>
                        <li class="nav-item">
                            <a href="Deco" class="nav-link">Se déconnecter</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>
        <main class="container-fluid">
            <h2>Hello <%= ((User) (session.getAttribute("user"))).getLogin() %> (administrateur) !</h2>
            <c:choose>
                <c:when test="${param.from == \"saisie\"}">
                    <% request.getRequestDispatcher("passage.jsp").forward(request, response); %>
                </c:when>
                <c:when test="${param.page == \"passage\"}">
                    <% request.setAttribute("passagesAffiches", passages.getPassagesByUser((User) session.getAttribute("user"))); %>
                    <c:import url="passage.jsp"/>
                </c:when>
                <c:when test="${param.page == \"passages\"}">
                    <% request.setAttribute("passagesAffiches", passages.getAllPassages()); %>
                    <c:import url="passage.jsp"/>
                </c:when>
                <c:when test="${param.page == \"user_me\"}">
                    <c:import url="user.jsp"></c:import>
                </c:when>
                <c:when test="${param.page == \"user\" && empty param.login}">
                    <form action="interface_admin.jsp?page=user" method="post">
                        <label for="login">Entrez le nom de l'utilisateur</label>
                        <input type="text" id="login" name="login">
                        <input type="submit" value="Valider">
                    </form>
                </c:when>
                <c:when test="${param.page == \"user\" && !empty param.login}">
                    <c:import url="user.jsp?login=${param.login}"/>
                </c:when>
                <c:when test="${param.page == \"passagesByUser\" && empty param.user2}">
                    <form action="interface_admin.jsp?page=passagesByUser" method="post">
                        <label for="user2">Entrez le nom de l'utilisateur</label>
                        <input type="text" id="user2" name="user2">
                        <input type="submit" value="Valider">
                    </form>
                </c:when>
                <c:when test="${param.page == \"passagesByUser\" && !empty param.user2}">
                    <% request.setAttribute("passagesAffiches", passages.getPassagesByUserLogin(request.getParameter("user2"))); %>
                    <c:import url="passage.jsp"/>
                </c:when>
                <c:when test="${param.page == \"passagesBySalle\" && empty param.salle}">
                    <form action="interface_admin.jsp?page=passagesBySalle" method="post">
                        <label for="salle">Entrez le nom de la salle</label>
                        <input type="text" id="salle" name="salle">
                        <input type="submit" value="Valider">
                    </form>
                </c:when>
                <c:when test="${param.page == \"passagesBySalle\" && !empty param.salle}">
                    <% request.setAttribute("passagesAffiches", passages.getPassagesBySalleName(request.getParameter("salle"))); %>
                    <c:import url="passage.jsp"/>
                </c:when>
                <c:when test="${param.page == \"salles\"}">
                    <c:import url="salles.jsp"/>
                </c:when>
                <c:when test="${param.page == \"contacts\"}">
                    <c:import url="users.jsp"></c:import>
                </c:when>
            </c:choose>
        </main>
        <footer>
            Jeremy Thomas & Julien Giraud - MIF03
        </footer>
    </body>
</html>
