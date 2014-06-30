<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%request.setCharacterEncoding("UTF-8");%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="EN">
    <head>
        
        <link rel="icon" type="image/vnd.microsoft.icon" href="/favicon.ico" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="content-language" content="ru" />
        <meta name="author" content="admin@diggfoto.net" />
        <meta name="description" content="Блог" />
        <meta name="keywords" content="urban,exploration, urban3p, диггерство, дигг, диггсталк, сталкер, руфинг, панорамы, сферы" />
        
        <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/main.css" />
        
        <title>Server Log <c:if test="${not empty title}"> | <c:out value="${title}" /></c:if></title>
    
        <c:if test="${not empty scripts}">
            <c:forEach items="${requestScope.scripts}" var="script">
                <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/${script}.js"></script>
            </c:forEach>
        </c:if>
       
        </head>
        <body>
            