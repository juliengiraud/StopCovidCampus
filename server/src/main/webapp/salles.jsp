<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>

<% if (request.getSession().getAttribute("user") == null) {
    response.sendRedirect("index.html");
    return;
}%>

<c:if test="${!sessionScope.admin}">
    <% request.getRequestDispatcher("interface.jsp").forward(request, response); %>
</c:if>

<h3>Liste des salles :</h3>

<table class="table">
    <thead class="thead-dark">
    <tr>
        <th>Nom</th>
    </tr>
    </thead>
    <tbody>
        <c:forEach items="<%= passages.getDistinctSalles() %>" var="salle">
            <tr>
                <th><a href="interface_admin.jsp?page=passagesBySalle&salle=${salle.nom}">${salle.nom}</a></th>
            </tr>
        </c:forEach>
    </tbody>
</table>

