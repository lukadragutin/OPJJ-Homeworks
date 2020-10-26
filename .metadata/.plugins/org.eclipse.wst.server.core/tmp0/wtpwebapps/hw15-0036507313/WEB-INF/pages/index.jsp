<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BlogMe Baby</title>
<style type="text/css">
.greska {
	font-family: fantasy;
	font-weight: bold;
	font-size: 0.9em;
	color: #FF0000;
	padding-left: 75px;
}

.formLabel {
	display: inline-block;
	width: 100px;
	font-weight: bold;
	text-align: right;
	padding-right: 10px;
}

.formControls {
	margin-top: 10px;
}
</style>
</head>
<body>
	<c:choose>
		<c:when test="${not empty current_user_id}">
			<c:out value="${current_user_fn} ${current_user_ln}"></c:out>
			<a href="logout">Logout</a>
		</c:when>
		<c:otherwise>Not logged in</c:otherwise>
	</c:choose>
	<c:if test="${empty current_user_id}">
		<p>If you have an existing account login:</p>
		<form action="login" method="post">
			Nickname: <input type="text" name="nick" value="${login_nick_value}"><br>
			Password: <input type="password" name="password"><br>
			<c:if test="${not empty login_error}">
				<div class="greska">
					<c:out value="${login_error}"></c:out>
				</div>
			</c:if>
			<input type="submit" value="Login"> <input type="reset">
		</form>

		<p>
			If you are a new user register <a href="register">here.</a>
		</p>
	</c:if>

	<p>List of registered users:</p>
	<ol>
		<c:forEach var="b" items="${users}">
			<li>Author: ${b.nick} -> <a href="author/${b.nick}">blog</a></li>
		</c:forEach>
	</ol>
</body>
</html>