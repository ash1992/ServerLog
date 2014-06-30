<div class="logo">Ошибка 500</div>

<div class="log SEVERE">
    ${pageContext.exception}:<br/>
    <hr/>
    <c:forEach var="trace" items="${pageContext.exception.stackTrace}">
    ${trace}<br/>
    </c:forEach>
</div>