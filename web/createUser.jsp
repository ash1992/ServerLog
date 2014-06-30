<div class="logo">Создание пользователя</div>
<c:if test="${not empty error}">
    <div class="log SEVERE">
        Ошибка:<br/>
        <hr/>
        <c:out value="${error}" />
    </div>
</c:if>
<div class="log">
    <form action="${pageContext.servletContext.contextPath}/createUser/" method="post" />
        Имя: &nbsp;<input type="text" name="login" value="${login}"  /><br/>
        <hr/>
        Пароль: &nbsp;<input type="text" name="password" value="${password}" /><br/>
        <hr/>
        <input type="submit" value="Создать" />
    </form>
</div>