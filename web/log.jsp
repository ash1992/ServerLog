<c:out value="${logFile.getName()}"/>:<br/>

<c:forEach items="${requestScope.log}" var="logEntry">
    <div class="log <c:out value="${logEntry.getType()}"/>">
        <div class="title">
            <c:out value="${logEntry.getDate()}"/>
        </div>
        <c:out value="${logEntry.getContent()}" escapeXml="false" /><br/>
    </div>
</c:forEach>
