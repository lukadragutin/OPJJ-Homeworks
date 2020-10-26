<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body style="background-color:#<%Object color = session.getAttribute("bgColor");
			if (color == null)
				color = "FFFFFF";
			out.print(color.toString());%>">
	<h1>Invalid Parameters</h1>
	
	<p>Wrong parameters selected for excel table generation.<br>
	Parameter info:<br><br>
	   a -> integer from [-100, 100]<br>
	   b -> integer from [-100, 100], larger than a<br>
	   n -> integer from [1, 5] </p>
</body>
</html>