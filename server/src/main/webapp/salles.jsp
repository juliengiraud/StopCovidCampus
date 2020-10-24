<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>

<h1>Liste des salles</h1>

<h2>Hello <%= ((User) (session.getAttribute("user"))).getLogin() %> !</h2>

<% List<Passage> passagesAffiches = null; %>

<c:if test="${sessionScope.admin}">
    <% passagesAffiches = passages.getAllPassages(); %>
</c:if>

<c:if test="${!sessionScope.admin}">
    <% passagesAffiches = passages.getPassagesByUser((User) session.getAttribute("user")); %>
</c:if>

<table>
    <tr>
        <th>Salle</th>
    </tr>

    <c:forEach items="<%= passagesAffiches %>" var="passage">
        <tr>
            <td>${passage.salle.nom}</td>
        </tr>
    </c:forEach>
</table>
