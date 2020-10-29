<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.GestionPassages" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Salle" %>
<%@ page import="java.util.Date" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="java.util.List" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>

<% if (request.getSession().getAttribute("user") == null) {
    response.sendRedirect("index.html");
    return;
}%>

<% if (request.getMethod().equals("POST")) { // Traitement du formulaire envoyé par saisie.html

    if(request.getParameter("entree") != null) {
        passages.add(new Passage(
                (User) session.getAttribute("user"),
                new Salle(request.getParameter("nom")),
                new Date())
        );
    } else if(request.getParameter("sortie") != null) {
        List<Passage> passTemp = passages.getPassagesByUserAndSalle(
                (User) session.getAttribute("user"),
                new Salle(request.getParameter("nom"))
        );
        if(!passTemp.isEmpty()) {
            Passage p = passTemp.get(0);
            p.setSortie(new Date());
        }
    }
    response.sendRedirect("interface.jsp?page=passage");
} %>

<h3>Liste de vos passages</h3>

<table class="table">
    <thead class="thead-dark">
        <tr>
            <th>Login</th>
            <th>Salle</th>
            <th>Entrée</th>
            <th>Sortie</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="<%= session.getAttribute(\"passagesAffiches\") %>" var="passage">
            <tr>
                <th>${passage.user.login}</th>
                <td>${passage.salle.nom}</td>
                <td><fmt:formatDate value="${passage.entree}" var="heureEntree" type="time" />
                        ${heureEntree}</td>
                <td><fmt:formatDate value="${passage.sortie}" var="heureSortie" type="time" />
                        ${heureSortie}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>