<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="<%= request.getAttribute("")%>" %>

<jsp:useBean id="users" type="java.util.Map<java.lang.String,fr.univlyon1.m1if.m1if03.classes.User>" scope="application"/>
<c:set var="user" value="${users[requestScope.login]}"/>

<section>
    <h1>User ${requestScope.login}</h1>

    <c:if test="${user == null}">
        <h1>Utilisateur ${requestScope.login} non trouvé</h1>
    </c:if>

    <c:if test="${user != null}">
        <ul>
            <li> Login : ${user.login}</li>
            <li>Nom : ${user.nom}</li>
            <li> admin : ${user.admin == true ? "oui" : "non"}</li>
        </ul>
    </c:if>
</section>