<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home Page</title>
</head>
<body
	style="background-color:#<%Object color = session.getAttribute("bgColor");
			if (color == null)
				color = "FFFFFF";
			out.print(color.toString());%>">
	<h1>Welcome</h1>
	
	<p>Information about server uptime on this
	<a href="appinfo.jsp">link</a></p><br>
	<a href="/webapp2/colors.jsp">Background color chooser</a><br><br>

	
	Parametri za crtanje tablice sinusa i kosinusa:<br><br>
	<form action="trigonometric" method="GET">
		Početni kut:<br>
		<input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br>
		<input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabeliraj"><input type="reset"
			value="Reset">
	</form><br>
	
	Trigonometrijska tablica za:<br>
		<a href="trigonometric?a=0&b=90">a = 0, b = 90</a><br><br>
		
	<h2>Stories</h2>
	<a href="stories/funny.jsp">Funny</a><br><br>
	
	<h3>Excel document of first n powers for a-b</h3>
	<a href="powers?a=1&b=100&n=3">a=1,b=100,n=3</a><br><br>
	<a href="glasanje"><h3>Anketa</h3></a>
	
</body>
</html>