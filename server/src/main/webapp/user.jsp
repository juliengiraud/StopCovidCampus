<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="passages" scope="application" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>

<html>
<head>
    <title>Users</title>
</head>
<body>

<h1>Liste des users</h1>

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
        <th>Users</th>
    </tr>

    <c:forEach items="<%= passagesAffiches %>" var="passage">
        <tr>
            <td>${passage.user.login}</td>
        </tr>
    </c:forEach>
</table>

<p><a href="saisie.html">Saisir un nouveau passage</a></p>
<p><a href="Deco">Se déconnecter</a></p>

</body>
</html>
