<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section>
    <h1>Liste des utilisateurs</h1>
    <table>
        <tr>
            <th>Login</th>
            <th>Nom</th>
            <th>admin</th>
        </tr>
        <c:forEach items="${requestScope.dto.users}" var="user">
            <tr>
                <td><a href="admin?contenu=user&login=${user.login}">${user.login}</a></td><!-- TODO mettre l'URI -->
                <td>${user.nom}</td>
                <td>${user.admin ? "oui" : "non"}</td>
            </tr>
        </c:forEach>
    </table>
</section>
