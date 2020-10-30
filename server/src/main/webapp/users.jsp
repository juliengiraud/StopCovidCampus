<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>

<% if (request.getSession().getAttribute("user") == null) {
    response.sendRedirect("index.html");
    return;
}%>

<%
    List<User> contacts = passages.getContacts((User) session.getAttribute("user"));
%>

<h3>Liste de vos contacts :</h3>

<table class="table">
    <thead class="thead-dark">
    <tr>
        <th>Login</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="<%= contacts %>" var="contact">
        <tr>
            <th><a href="interface_admin.jsp?page=passagesByUser&user2=${contact.login}">${contact.login}</a></th>
        </tr>
    </c:forEach>
    </tbody>
</table>

