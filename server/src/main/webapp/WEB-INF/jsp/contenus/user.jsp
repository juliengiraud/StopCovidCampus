<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="users" type="java.util.Map<java.lang.String,fr.univlyon1.m1if.m1if03.classes.User>" scope="application"/>
<c:set var="salle" value="${users[param.login]}"/>

<section>
    <h1>User ${param.login}</h1>

    <c:if test="${salle == null}">
        <h1>Utilisateur ${param.login} non trouvé</h1>
    </c:if>

    <c:if test="${salle != null}">
        <ul>
            <li>Login : ${salle.login}</li>
            <li>Nom : ${salle.nom}</li>
            <li>admin : ${salle.admin == true ? "oui" : "non"}</li>
        </ul>
    </c:if>
</section>