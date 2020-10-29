<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="passages" scope="application" type="fr.univlyon1.m1if.m1if03.classes.GestionPassages"/>
<% List<Passage> p = passages.getPassagesByUser((User) session.getAttribute("user"));
if (p.size() == 0) {
    response.sendError(404, "Utilisateur non trouvé !");
    return;
}
%>

<h1>Utilisateur courant :</h1>
<%= p.get(0).getUser().getLogin() %>