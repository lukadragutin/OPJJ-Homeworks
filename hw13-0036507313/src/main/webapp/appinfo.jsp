<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Server Up-time Information</title>
</head>
<body
	style="background-color:#<%Object color = session.getAttribute("bgColor");
			if (color == null)
				color = "FFFFFF";
			out.print(color.toString());%>">

	<h1>Server Up-time</h1>
	<p>
		This server has been running for: <br>
		<b><%
			long start = (Long) session.getServletContext().getAttribute("time");
			long diff = System.currentTimeMillis() - start;
			long millis = diff % 1000;
			long second = (diff / 1000) % 60;
			long minute = (diff / (1000 * 60)) % 60;
			long hour = (diff / (1000 * 60 * 60)) % 24;
			long day = (diff / (1000 * 60 * 60 * 24));

			out.println(String.format("%d days, %d hours, %d minutes, %d seconds and %d milliseconds", day, hour,
					minute, second, millis));
		%></b>
	</p>
	
	<a href="../index.jsp">Home</a>
</body>
</html>