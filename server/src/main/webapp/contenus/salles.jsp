<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Salle" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:useBean id="salles" type="java.util.Map<java.lang.String, fr.univlyon1.m1if.m1if03.classes.Salle>"
             scope="request"/>
<section>
    <h1>Liste des salles</h1>

    <%
        try {
            if (request.getMethod().equals("POST") && request.getParameter("nomSalle") != null && request.getParameter("capacite") != null) {
                Salle salle = salles.get(request.getParameter("nomSalle"));
                salle.setCapacite(Integer.parseInt(request.getParameter("capacite")));
            }
        } catch (NumberFormatException e) {
    %>
    <strong>Erreur : valeur incorrecte</strong>
    <%
        }
    %>

    <table>
        <tr>
            <th>Nom</th>
            <th>Capacité</th>
            <th>Nb présents</th>
        </tr>

        <c:forEach items="${salles.entrySet()}" var="salleEntry">
            <tr>
                <td>${salleEntry.value.nom}</td>
                <td>
                    <form action="admin" method="post">
                        <input type="text" name="capacite" size="3" value="${salleEntry.value.capacite != -1 ? salleEntry.value.capacite : ''}"/>
                        <input type="hidden" name="contenu" value="salles">
                        <input type="hidden" name="nomSalle" value="${salleEntry.value.nom}">
                        <input type="submit" value="Modifier">
                    </form>
                </td>
                <td>${salleEntry.value.presents} présent(s)</td>
                <td>
                    <c:if test="${salleEntry.value.saturee}">
                        <strong style="color: red">Capacité dépassée</strong>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</section>