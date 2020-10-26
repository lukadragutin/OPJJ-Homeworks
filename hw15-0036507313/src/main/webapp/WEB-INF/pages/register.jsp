<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
</head>
<body>
	<c:choose>
		<c:when test="${not empty current_user_id}">
			<c:out value="${current_user_fn} ${current_user_ln}"></c:out>
			<a href="logout">Logout</a>
		</c:when>
		<c:otherwise>Not logged in</c:otherwise>
	</c:choose>

	<h1>Create a new account</h1>

	<form action="register" method="post">
		First name: <input type="text" name="firstName"
			value="${form.firstName}"><br>
		<c:if test="${form.hasError('firstName')}">
			<c:out value="${form.getError('firstName')}"></c:out>
		</c:if>
		Last name: <input type="text" name="lastName" value="${form.lastName}"><br>
		<c:if test="${form.hasError('lastName')}">
			<c:out value="${form.getError('lastName')}"></c:out>
		</c:if>
		Email: <input type="text" name="email" value="${form.email}"><br>
		<c:if test="${form.hasError('email')}">
			<c:out value="${form.getError('email')}"></c:out>
		</c:if>
		Nickname: <input type="text" name="nick" value="${form.nick}"><br>
		<c:if test="${form.hasError('nick')}">
			<c:out value="${form.getError('nick')}"></c:out>
		</c:if>
		Password: <input type="password" name="password"><br>
		<c:if test="${form.hasError('password')}">
			<c:out value="${form.getError('password')}"></c:out>
		</c:if>
		<input type="submit" value="Register"> <input type="reset">
	</form>
	
	<a href="servleti/main">Home</a>
</body>
</html>