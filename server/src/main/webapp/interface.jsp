<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Jérémy
  Date: 16/10/2020
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Présence UCBL</title>
    </head>
    <body>
        <header>
            <nav>
                <a href="interface.jsp?page=passage">Voir les passages</a>
                <a href="interface.jsp?page=user">voir mes informations</a>
                <a href="saisie.html">Saisir un nouveau passage</a>
                <a href="Deco">Se déconnecter</a>
            </nav>
        </header>
        <h1>Interface utilisateur normal</h1>
        <main>
            <c:choose>
                <c:when test="${param.saisiePassage == \"passage\"}">
                    <%request.getRequestDispatcher("passage.jsp").forward(request, response);%>
                </c:when>
                <c:when test="${param.page == \"passage\"}">
                    <c:import url="passage.jsp"></c:import>
                </c:when>
                <c:when test="${param.page == \"user\"}">
                    <c:import url="user.jsp"></c:import>
                </c:when>
            </c:choose>

        </main>
        <footer>
            Jeremy Thomas & Julien Giraud - MIF03
        </footer>
    </body>
</html>
