<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>



<% List<Passage> passagesAffiches = null; %>

<c:if test="${sessionScope.admin}">
    <c:if test="${empty param.login}">
        <h2>Liste des utilisateurs</h2>
        <% passagesAffiches = passages.getAllPassages(); %>
    </c:if>
    <c:if test="${!empty param.login}">
        <h2>Utilisateur ${param.login}</h2>
        <% passagesAffiches = passages.getPassagesByUserLogin(request.getParameter("login")); %>
    </c:if>
</c:if>

<c:if test="${!sessionScope.admin}">
    <% passagesAffiches = passages.getPassagesByUser((User) session.getAttribute("user")); %>
</c:if>

<table>
    <c:forEach items="<%= passagesAffiches %>" var="passage">
        <tr>
            <td>${passage.user.login}</td>
        </tr>
    </c:forEach>
</table>
