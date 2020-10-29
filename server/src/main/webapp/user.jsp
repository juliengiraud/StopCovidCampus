<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>

<%
    List<Passage> passageList = passages.getAllPassages();
    Set<User> users = new HashSet<>();
    for(Passage p : passageList){
        users.add(p.getUser());
    }
%>

<c:set var="userToDisplay" value="${null}"/>

<c:if test="${sessionScope.admin && !empty param.login}">
    <h2>Recherche pour "${param.login}" :</h2>
    <c:forEach items="<%= users %>" var = "u">
        <c:if test="${u.login == param.login }">
            <c:set var="userToDisplay" value="${u}"/>
        </c:if>
    </c:forEach>
</c:if>
<c:if test="${empty param.login}">
    <h2>Utilisateur courant :</h2>
    <c:forEach items="<%= users %>" var = "u">
        <c:if test="${u.login == sessionScope.user.login }">
            <c:set var="userToDisplay" value="${u}"/>
        </c:if>
    </c:forEach>
</c:if>

<c:if test="${user != null}">
    <p>${userToDisplay.login}</p>
</c:if>
