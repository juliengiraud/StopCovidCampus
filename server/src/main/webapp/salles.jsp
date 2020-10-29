<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Salle" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>

<c:if test="${!sessionScope.admin}">
    <c:redirect url="index.html"/>
</c:if>

<h2>Liste des salles :</h2>

<%
    List<Passage> passageList = passages.getAllPassages();
    Set<Salle> salles = new HashSet<>();
    for(Passage p : passageList){
        salles.add(p.getSalle());
    }
%>
<ul>
    <c:forEach items="<%= salles %>" var="salle">
        <li>${salle.nom}</li>
    </c:forEach>
</ul>
