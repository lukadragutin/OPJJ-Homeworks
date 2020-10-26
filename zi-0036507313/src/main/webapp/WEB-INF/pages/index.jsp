<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Grafika</title>

</head>
<body>
	<p>List of available files: </p>
	<ol>
		<c:forEach var="b" items="${files}">
			<li><a href="open?name=${b.name}">${b.name}</a></li>
		</c:forEach>
	</ol>
	
	<form action="addFile" method="post">
		<input type="text" name="title"><br>
		<textarea rows="10" cols="50" name="text"></textarea><br>
		<input type="submit"> <input type="reset">
	</form>
</body>
</html>