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

<c:if test="${!sessionScope.admin}">
    <c:redirect url = "interface.jsp"/>
</c:if>

<html>
<head>
    <title>Title</title>
</head>
<body>
<header>
    <nav>
        <a href="interface_admin.jsp?page=passage">Voir tous les passages</a>
        <a href="interface_admin.jsp?page=salles">Voir toutes les salles</a>
        <a href="interface_admin.jsp?page=user">Voir un utilisateur</a>
        <a href="saisie.html">Saisir un nouveau passage</a>
        <a href="Deco">Se déconnecter</a>
    </nav>
    <h1>Interface utilisateur administrateur</h1>
    <main>
        <c:choose>
            <c:when test="${param.from == \"saisie\"}">
                <% request.getRequestDispatcher("passage_admin.jsp").forward(request, response); %>
            </c:when>
            <c:when test="${param.page == \"passage\"}">
                <c:import url="passage_admin.jsp"/>
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
            <c:when test="${param.page == \"salles\"}">
                <c:import url="salles.jsp"/>
            </c:when>
        </c:choose>
    </main>
    <footer>
        Jeremy Thomas & Julien Giraud - MIF03
    </footer>
</header>
</body>
</html>
