<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Voting Site</title>
</head>
<body>
	<h1>Vote For Your Favorites</h1>
	<p>Select a voting category:</p>
	<ol><c:forEach var="e" items="${polls}">
	<li><a href="glasanje?pollID=${e.id}">${e.title}</a>
	</c:forEach></ol>
</body>
</html>