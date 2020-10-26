<%@page import="java.util.List"%>
<%@page import="hr.fer.zemris.java.p12.PollOption"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Rezultati Glasanja</title>
</head>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<thead>
			<tr>
				<th>Opcija</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="i" items="${pollOptions}">
				<tr>
					<td>${i.optionTitle}</td>
					<td>${i.votesCount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<%
		session.setAttribute("poll", request.getAttribute("poll"));
		session.setAttribute("pollOptions", request.getAttribute("pollOptions"));
	%>
	<h2>Grafički prikaz rezulata</h2>
	<img alt="PieChart" src="glasanje-grafika">

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a>
	</p>

	<h2>Razno</h2>
	<p>Primjeri pobjedničkih izbora:</p>
	<ol>
		<c:forEach var="b" items="${best}">
		<li><a href="${b.optionLink}" target="_blank">${b.optionTitle}</a>
		</c:forEach>
	</ol><br>
	
	<a href="${pageContext.request.contextPath}/index.html">Home</a>
</body>
</html>