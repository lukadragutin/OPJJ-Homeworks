<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body>
	<h2>Oopsy Doopsy</h2>
	
	<p><c:out value="${message}"></c:out></p>
	<a href="servleti/main">Home</a>
</body>
</html>