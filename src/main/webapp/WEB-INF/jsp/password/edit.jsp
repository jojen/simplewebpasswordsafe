<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title><spring:message code="app.title"/></title>
    <link rel="stylesheet" href="/static/css/styles.css" type="text/css"/>
</head>
<body>
<h1><spring:message code="password.edit.page.title"/></h1>
<a href="/"><spring:message code="password.list.link.label"/></a>
<div>
    <form:form action="/password/edit" commandName="password" method="POST">
        <form:hidden path="id"/>
        <div>
            <form:label path="title"><spring:message code="password.label.title"/>:</form:label>
            <form:input path="title" size="20"/>
            <form:errors path="title" cssClass="error" element="div"/>
        </div>
        <div>
            <form:label path="description"><spring:message code="password.label.description"/>:</form:label>
            <form:textarea path="description" />
            <form:errors path="description" cssClass="error" element="div"/>
        </div>
        <div>
            <form:label path="username"><spring:message code="password.label.username"/>:</form:label>
            <form:input path="username" size="20"/>
            <form:errors path="username" cssClass="error" element="div"/>
        </div>
        <div>
            <input type="submit" value="<spring:message code="password.edit.page.submit.label"/>"/>
        </div>
    </form:form>
</div>
</body>
</html>