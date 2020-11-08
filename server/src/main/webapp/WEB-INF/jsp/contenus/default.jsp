<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<section id="contenu">
    <p><strong>Hello ${requestScope.user.nom} !</strong></p>
    <c:if test="${requestScope.myPassages.size() > 0}">
        <p>Vous êtes actuellement dans les salles :</p>
        <ul>
            <c:forEach var="p" items="${requestScope.myPassages}">
                <li>
                        ${p.salle.nom}
                    <c:if test="${p.salle.saturee}">
                        <strong style="color: red">Alerte : cette salle est saturée !</strong>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </c:if>
    <p>Choisissez une option dans le menu.</p>
</section>
