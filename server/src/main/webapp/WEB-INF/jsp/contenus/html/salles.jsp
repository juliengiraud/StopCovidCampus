<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<section>
    <h1>Liste des salles</h1>
    <table>
        <tr>
            <th>Nom</th>
            <th>Capacité</th>
            <th>Nb présents</th>
        </tr>
        <%--
            Ici, on utilise le bean de scope application dans le contexte :
            Comme on a besoin de toutes les salles, inutile de le recopier en attribut de chaque requête envoyée à salles.jsp
        --%>
        <c:forEach items="${requestScope.dto.salles}" var="salle">
            <tr>
                <td><a href="admin?contenu=salle&nomSalle=${salle.nom}">${salle.nom}</a></td><!-- TODO mettre l'URI -->
                <td>
                    <form action="salles" method="put" accept-charset="utf-8">
                        <input type="text" name="capacite" size="3"
                               value="${salle.capacite != -1 ? salle.capacite : ''}"/>
                        <input type="hidden" name="contenu" value="salles">
                        <input type="hidden" name="nomSalle" value="${salle.nom}">
                        <input type="submit" value="Modifier">
                    </form>
                </td>
                <td>${salle.presents} présent(s)</td>
                <td>
                    <c:if test="${salle.saturee}">
                        <strong style="color: red">Capacité dépassée</strong>
                    </c:if>
                </td>
                <td>
                    <form action="salles" method="delete" accept-charset="UTF-8">
                        <input type="hidden" name="contenu" value="salles">
                        <input type="hidden" name="nomSalle" value="${salle.nom}">
                        <input type="submit" value="Supprimer">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <hr>
    <h2>Ajouter une salle</h2>
    <form action="salles" method="post" accept-charset="UTF-8">
        <input type="hidden" name="contenu" value="salles">
        <label> Nom de la salle :
            <input type="text" name="nomSalle">
        </label>
        <input type="submit" value="Ajouter">
    </form>
</section>