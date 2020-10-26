<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Glasanje</title>
</head>
<body style="background-color:#<%Object color = session.getAttribute("bgColor");
			if (color == null)
				color = "FFFFFF";
			out.print(color.toString());%>">
	<h1>Glasanje za omiljeni bend:</h1>
	<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na
		link kako biste glasali!</p>
	<ol>
		<c:forEach var="e" items="${bands}">
			<li><a href="glasanje-glasaj?id=<c:out value="${e.key}"/>"><c:out
						value="${e.value.name}"/></a></li>
		</c:forEach>
	</ol>
</body>
</html>