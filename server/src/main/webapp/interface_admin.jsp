<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Jérémy
  Date: 16/10/2020
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>

<c:if test="${!sessionScope.admin}">
    <% response.sendRedirect("interface.jsp"); %>
</c:if>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Présence UCBL</title>
    </head>
    <body>
        <header>
            <nav>
                <a href="interface_admin.jsp?page=passage">Voir les passages</a>
                <a href="interface_admin.jsp?page=users">Les informations des utilisateurs</a>
                <a href="interface_admin.jsp?page=user">Les informations d'un utilisateur</a>
                <a href="interface_admin.jsp?page=salles">voir les salles</a>
                <a href="saisie.html">Saisir un nouveau passage</a>
                <a href="Deco">Se déconnecter</a>
            </nav>
        </header>
        <h1>Interface administrateur</h1>
        <main>
            <c:choose>
                <c:when test="${param.saisiePassage == \"passage\"}">
                    <%request.getRequestDispatcher("passage.jsp").forward(request, response);%>
                </c:when>
                <c:when test="${param.page == \"passage\"}">
                    <c:import url="passage_admin.jsp"></c:import>
                </c:when>
                <c:when test="${param.page == \"users\"}">
                    <c:import url="user.jsp"></c:import>
                </c:when>
                <c:when test="${param.page == \"user\" && empty param.login}">
                     <form action="interface_admin.jsp?page=user" method="post">
                         <label for="login">Login :</label><input id="login" type="text" name="login">
                         <input type="submit" value="Valider">
                     </form>
                </c:when>
                <c:when test="${param.page == \"user\" && !empty param.login}">
                    <c:import url="user.jsp?name=${param.login}"></c:import>
                </c:when>
                <c:when test="${param.page == \"salles\"}">
                    <c:import url="salles.jsp"></c:import>
                </c:when>
            </c:choose>

        </main>
        <footer>
            Jeremy Thomas & Julien Giraud - MIF03
        </footer>
    </body>
</html>
