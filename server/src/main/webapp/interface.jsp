<%--
  Created by IntelliJ IDEA.
  User: Jérémy
  Date: 16/10/2020
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head>
        <title>Présence UCBL</title>
    </head>
    <body>
        <h1>Interface</h1>
        <nav>
            <a href="interface.jsp?page=passage">Voir les passages</a>
            <a href="interface.jsp?page=user">voir mes informations</a>
            <a href="Deco">Se déconnecter</a>
        </nav>
        <main>
            <%
                String p = "";
                if (request.getMethod().equals("GET")) { // Traitement du formulaire envoyé par saisie.html
                    if(request.getParameter("page") != null) {
                        p = request.getParameter("page");
                        switch (p) {
                            case "passage":
                                p = "passage.jsp";
                                break;
                            case "user":
                                p = "user.jsp";
                                break;
                            default:
                        }
                    }
                }
             %>

            <jsp:include page="<%= p %>" ></jsp:include>

        </main>
        <footer>
            Jeremy Thomas & Julien Giraud - MIF03
        </footer>
    </body>
</html>
