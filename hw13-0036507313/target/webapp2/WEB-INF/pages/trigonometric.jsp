<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trigonometry table</title>
</head>
<body
	style="background-color:#<%Object color = session.getAttribute("bgColor");
			if (color == null)
				color = "FFFFFF";
			out.print(color.toString());%>">

	<h1>Trigonometrijska tablica</h1>

	<table>
		<thead><tr><td><b>a</b></td><td><b>sin(a)</b></td><td><b>cos(a)</b></td></tr>
		</thead>
		<c:forEach var="t" items="${trigonometry}">
			<tr><td><b>${t.key}</b></td><td>${t.value.sin}</td><td>${t.value.cos}</td></tr>
		</c:forEach>
	</table>
</body>
</html>