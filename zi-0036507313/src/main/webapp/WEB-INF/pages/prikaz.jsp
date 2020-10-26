<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>${file.name}</p>
	
	<p> Linije: <c:out value="${lines}"></c:out><br>
		Kružnice: <c:out value="${circles}"></c:out><br>
		Krugovi: <c:out value="${fCircles}"></c:out><br>
		Trokuti: <c:out value="${triangles}"></c:out><br></p>
		
		<img src="slika?name=${file.name}">
		
		<a href="main">Home</a>
</body>
</html>