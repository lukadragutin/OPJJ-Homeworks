<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Blog Page</title>
</head>
<body>
	<c:choose>
		<c:when test="${not empty current_user_id}">
			<c:out value="${current_user_fn} ${current_user_ln}"></c:out>
			<a href="logout">Logout</a>
		</c:when>
		<c:otherwise>Not logged in</c:otherwise>
	</c:choose>
	<h1>${user.nick}'sBlog</h1>

	<c:choose>
		<c:when test="${entries.isEmpty()}">
			<div>No entries yet.</div>
		</c:when>
		<c:otherwise>
			<p>Here is the list of ${user.nick}'s blog entries:</p>
			<ol>
				<c:forEach var="e" items="${entries}">
					<li><a href="${user.nick}/${e.id}">${e.title}</a>
						(${e.createdAt}) <c:if test="${user.nick == current_user_nick}">
							<a href="${user.nick}/edit?id=${e.id}">EDIT</a>
						</c:if></li>
				</c:forEach>
			</ol>
			<hr>
		</c:otherwise>
	</c:choose>
	<c:if test="${user.nick == current_user_nick}">
	Add a new entry <a
			href="${pageContext.request.contextPath}/servleti/author/${user.nick}/new">here</a>.
	</c:if>
	
	<a href="servleti/main">Home</a>
</body>
</html>