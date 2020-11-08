<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<header class="wrapper">
    <div class="header-titre"><strong>Présence UCBL</strong></div>
    <c:if test="${requestScope.user != null}">
        <div class="header-user">
            <a href="${(requestScope.user.admin ? 'admin' : 'presence')
                    .concat('?contenu=user&login=')
                    .concat(requestScope.user.login)}">${requestScope.user.login}</a>
        </div>
    </c:if>
</header>
