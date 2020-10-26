<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.ArrayList"%>
<%@page import="hr.fer.zemris.java.webapp2.servlets.beans.BandInfo"%>
<%@page import="java.util.Map"%>
<%@page import="hr.fer.zemris.java.webapp2.servlets.LoadUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Rezultati Glasanja</title>
</head>
<body style="background-color:#<%Object color = session.getAttribute("bgColor");
			if (color == null)
				color = "FFFFFF";
			out.print(color.toString());%>">
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<%
		session.setAttribute("votes", LoadUtil.loadVotes(request));
		session.setAttribute("bands", LoadUtil.loadBands(request));
	%>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="i" items="${bands}">
				<c:set var="band" value="${i.key}" />
				<c:set var="vote" value="${votes[band]}" />
				<tr>
					<td>${i.value.name}</td>
					<td>${vote == null ? 0 : vote}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<%
		session.setAttribute("bands", request.getAttribute("bands"));
		session.setAttribute("votes", request.getAttribute("votes"));
	%>
	<h2>Grafički prikaz rezulata</h2>
	<img alt="PieChart" src="glasanje-grafika">

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a>
	</p>

	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ol>
		<%
			Map<String, Integer> votes = (Map<String, Integer>) session.getAttribute("votes");
			Map<String, BandInfo> bands = (Map<String, BandInfo>) session.getAttribute("bands");
			int max = 0;
			ArrayList<String> ids = new ArrayList<>();
			for (Entry<String, Integer> e : votes.entrySet()) {
				if (e.getValue().intValue() > max) {
					ids.clear();
					ids.add(e.getKey());
					max = e.getValue().intValue();
				} else if (e.getValue().intValue() == max)
					ids.add(e.getKey());
			}
			for (String id : ids) {
				out.print(
						"<li><a href=\"" + bands.get(id).getSongPath() + "\">" + bands.get(id).getName() + "</a></li>");
			}
		%>
	</ol><br>
	
	<a href="index.jsp">Home</a>
</body>
</html>