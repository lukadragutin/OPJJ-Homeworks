<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Voting</title>
</head>
<body>
	<h1>${poll.title}</h1>
	<p>${poll.message}</p>
	<ol>
		<c:forEach var="e" items="${pollOptions}">
			<li><a href="glasanje-glasaj?pollID=${poll.id}&id=${e.id}">
			<c:out value="${e.optionTitle}"/></a></li>
		</c:forEach>
	</ol><br>
	
	<a href="${pageContext.request.contextPath}/index.html">Home</a>
</body>
</html>