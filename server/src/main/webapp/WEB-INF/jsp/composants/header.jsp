<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<header class="wrapper">
    <div class="header-titre"><strong>Présence UCBL</strong></div>
    <c:if test="${param.user != null}">
        <div class="header-user"><a href="${param.user.admin ? 'admin?contenu=user&login='.concat(param.user.login) : 'presence?contenu=user&login='.concat(param.user.login)}"><%= ((User) (request.getAttribute("user"))).getLogin() %></a></div>
    </c:if>
</header>