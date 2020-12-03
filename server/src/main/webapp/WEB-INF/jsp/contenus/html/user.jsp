<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="users" type="java.util.Map<java.lang.String,fr.univlyon1.m1if.m1if03.classes.User>" scope="application"/>
<c:set var="user" value="${requestScope.dto.user}"/>

<section>
    <h1>User ${user.login}</h1>
    <ul>
        <li> Login : ${user.login}</li>
        <li>Nom : ${user.nom}</li>
        <li> admin : ${user.admin ? "oui" : "non"}</li>
    </ul>
</section>
