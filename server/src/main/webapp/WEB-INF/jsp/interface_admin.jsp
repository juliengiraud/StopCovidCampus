<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="/WEB-INF/jsp/erreurs/error.jsp" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Présence UCBL</title>
    <link rel="stylesheet" type="text/css" href="static/presence.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/composants/header.jsp"/>

<main class="wrapper">
    <jsp:include page="/WEB-INF/jsp/composants/menu_admin.jsp"/>
    <article class="contenu">
        <c:choose>
            <c:when test="${param.contenu == null }">
                <jsp:include page="/WEB-INF/jsp/contenus/default_admin.jsp"/>
            </c:when>
            <c:when test="${param.contenu == \"passages\"}">
                <jsp:include page="/WEB-INF/jsp/contenus/passages.jsp"/>
            </c:when>
            <c:when test="${param.contenu == \"user\"}">
                <jsp:include page="/WEB-INF/jsp/contenus/user.jsp?login=${param.login}"/>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/jsp/contenus/${param.contenu}.jsp"/>
            </c:otherwise>
        </c:choose>
    </article>
</main>

<jsp:include page="/WEB-INF/jsp/composants/footer.html"/>
</body>
</html>
