<%@ page import="fr.univlyon1.m1if.m1if03.classes.dto.GenericDTO" %>
<%@ page contentType="application/xml; charset=UTF-8" %>

<%= ((GenericDTO) request.getAttribute("dto")).getXML() %>
