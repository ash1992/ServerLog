

<div class="logo">
    Log Manager v. 1.0
</div>

<form action="deleteLogs/" method="post">
    
    <table width="100%" border="1">
        <tbody>
        <c:forEach items="${requestScope.logs}" var="log">
            <tr>
                <th colspan="7" class="group">
                    <c:out value="${log.getName()}" />:
                </th>
            </tr>
            <tr>
                <th class="multicheck"><input name="selectAll_${log.getName()}" value="1" type="checkbox" id="file_${log.getName()}" onclick="selectAll('file_${log.getName()}');" /></th>
                <th>Имя</th>
                <th>Дата</th>
                <th>Последнее изменение</th>
                <th>Размер</th>
                <th colspan="2">Действия</th>
            </tr>
            <c:forEach items="${log.getLogFiles()}" var="logEntity" >
            <tr>
                <td class="multicheck"><input type="checkbox" name="file_${log.getName()}" value="${logEntity.getRealName()}" onclick="reSelectAll('file_${log.getName()}');" /></td>
                <td><c:out value="${logEntity.getRealName()}"/></td>
                <td><fmt:formatDate type="both" value="${logEntity.getDate()}" /></td>
                <td><fmt:formatDate type="both" value="${logEntity.getLastModifed()}" /></td>
                <td><c:out value="${logEntity.getSize()}"/></td>
                <td><a href="readLog/?log=<c:out value="${logEntity.getRealName()}"/>">Открыть</a></td>
                <td><a href="deleteLog/?log=<c:out value="${logEntity.getRealName()}"/>">Удалить</a></td>
            </tr>
            </c:forEach>

        </c:forEach>
        
        </tbody>
    </table>

    <div class="commands">
        <input type="submit" name="delete" value="Удалить выбранные" />
    </div>
</form>

<c:if test="${not empty param.delete}">
    <c:choose>
        <c:when test="${param.delete eq 'success'}">
            <script type="text/javascript">
                alert("Успешно удаленно!");
            </script>
        </c:when>
        <c:when test="${param.delete eq 'fail'}">
            <script type="text/javascript">
                alert("Не вышло удалить элемент...");
            </script>
        </c:when>
        <c:when test="${param.delete eq 'not_completely'}">
            <script type="text/javascript">
                alert("Некоторые элементы не были удаленны...");
            </script>
        </c:when>
    </c:choose>
</c:if>