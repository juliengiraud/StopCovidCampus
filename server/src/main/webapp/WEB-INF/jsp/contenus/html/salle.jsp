<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<c:set var="salle" value="${requestScope.dto.salle}"/>

<section>
    <h1>Salle ${salle.nom}</h1>
    <ul>
        <li>Nom : ${salle.nom}</li>
        <li>Capacité : ${salle.capacite == -1 ? "Non spécifiée" : salle.capacite}</li>
        <li>Nb présents : ${salle.presents}</li>
        <li>Saturée : ${salle.saturee ? "oui" : "non"}</li>
    </ul>
</section>
