<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="app.title"/></title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/static/js/vendor/instafilta.min.js"></script>

    <script type="text/javascript" src="/static/js/password-manager.js"></script>
    <link rel="stylesheet" href="/static/css/styles.css" type="text/css"/>
</head>
<body>
<div class="messages">
    <c:if test="${feedbackMessage != null}">
        <div class="alert alert-success" role="alert">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
            <c:out value="${feedbackMessage}"/>
        </div>
    </c:if>
    <c:if test="${errorMessage != null}">
        <div class="alert alert-danger" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <c:out value="${errorMessage}"/>
        </div>
    </c:if>
</div>
<h1><spring:message code="password.list.page.title"/></h1>


<div class="jumbotron">
    <div class="passwords">


        <div class="container">

            <div class="row head">

                <input type="text" id="filter" placeholder="Type here to filter">
            </div>
            <div class="row">

            </div>
            <a class="new btn btn-success pull-right" href="/password/create">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                <spring:message code="password.label.new"/>
            </a>

            <div id="password-list">

                <c:forEach items="${passwords}" var="password">

                    <div class="password-item">
                        <a class="btn btn-default pull-right" href="/password/delete/<c:out value="${password.id}"/>">
                            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                        </a>

                        <h3 class="instafilta-target">${password.title} <a class="btn btn-default"
                                                                           href="/password/edit/<c:out value="${password.id}"/>">
                            <span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
                            <spring:message code="password.label.edit"/>
                        </a></h3>
                        <p> ${password.description}</p>



                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label for="usr-${password.id}"> <spring:message
                                            code="password.label.username"/></label>
                                <textarea id="usr-${password.id}" class="instafilta-target form-control" readonly
                                          rows="1">${password.username}</textarea>
                                </div>
                            </div>

                            <div class="col-xs-6">
                                <div class="form-group">

                                    <label for="password-${password.id}">
                                        <spring:message code="password.label.passwordValue"/><br>
                                    </label>


                                    <div class="input-group">


                                        <input id="password-${password.id}" type="password" class="form-control"
                                               value="xxxxxxxx">
                                      <span class="input-group-btn">
                                        <button class="show-pw btn btn-default" type="button"
                                                data-id="${password.id}"
                                                data-url="/password/get/${password.id}/value">
                                            <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                            <spring:message code="password.label.show"/>
                                        </button>
                                           <button class="hide-pw btn btn-default" data-id="${password.id}" type="button">
                                               <span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
                                               <spring:message code="password.label.hide"/>
                                           </button>

                                      </span>

                                    </div>
                                    <button class="update-pw btn btn-primary"
                                            data-url="/password/update/${password.id}/value"
                                            data-id="${password.id}">
                                        <span class="glyphicon glyphicon-upload" aria-hidden="true"></span>
                                        <spring:message code="password.label.update"/>
                                    </button>


                                </div>
                            </div>
                        </div>

                    </div>

                </c:forEach>


            </div>
        </div>
    </div>
</div>
</body>
</html>