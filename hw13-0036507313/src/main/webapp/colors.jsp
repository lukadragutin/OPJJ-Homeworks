<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Color Chooser</title>
</head>
<body style="background-color:#<% Object color = session.getAttribute("bgColor");
				if(color == null) color = "FFFFFF";
				out.print(color.toString());%>">
	<% %>
	<h1>Cool Color Chooser</h1>
	<p>Please select a color:</p>
	
	
		<a href="setcolor?pickedBgCol=FFFFFF">WHITE</a><br>
		<a href="setcolor?pickedBgCol=FF0000">RED</a><br>
		<a href="setcolor?pickedBgCol=008000">GREEN</a><br>
		<a href="setcolor?pickedBgCol=00FFFF">CYAN</a><br>
	
</body>
</html>