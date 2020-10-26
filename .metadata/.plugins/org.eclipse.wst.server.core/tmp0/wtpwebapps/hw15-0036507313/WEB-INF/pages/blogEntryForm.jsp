<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Blog Entry Form</title>
</head>
<body>
<c:choose>
		<c:when test="${not empty current_user_id}">
			<c:out value="${current_user_fn} ${current_user_ln}"></c:out>
			<a href="logout">Logout</a>
		</c:when>
		<c:otherwise>Not logged in</c:otherwise>
	</c:choose>
	<h1>
		<c:choose>
			<c:when test="${empty entry}">Create</c:when>
			<c:otherwise>Edit </c:otherwise>
		</c:choose>
		Blog Entry
	</h1>

	<form
		action="${pageContext.request.contextPath}/servleti/author/update?nick=${user.nick}<c:if test="${not empty entry}">&id=${entry.id}</c:if>"
		method="post">
		Title: <input type="text" name="title" value="${entry.title}"><br>
		Message:
		<textarea rows="10" cols="30" name="text">${entry.text}</textarea>
		<br> <input type="submit"><input type="reset">
	</form>
	
	<a href="servleti/main">Home</a>
</body>
</html>