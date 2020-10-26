<%@page import="java.util.HashMap"%>
<%@page import="java.util.Random"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%!
private static HashMap<Integer, String> colors = new HashMap<>();

static{
	colors.put(0, "black");
	colors.put(1, "green");
	colors.put(2, "blue");
	colors.put(3, "red");
	colors.put(4, "orange");
	colors.put(5, "yellow");
	colors.put(6, "grey");
	colors.put(7, "cyan");
	colors.put(8, "magenta");
	colors.put(9, "green");
}

%>

<html>
<head>
<meta charset="UTF-8">
<title>Funny Story</title>
</head>
<body style="background-color:#<%Object color = session.getAttribute("bgColor");
			if (color == null)
				color = "FFFFFF";
			out.print(color.toString());%>">
	<h1">Einstein's Conference</h1>
	
	<p style="color:<% Random rand = new Random();
					   int num = rand.nextInt(10);
					   out.print(colors.get(num));%>">
	One day, Einstein has to speak at an important science conference.<br>

On the way there, he tells his driver that looks a bit like him:<br>
 
"I'm sick of all these conferences. I always say the same things over and over!"<br>

The driver agrees: "You're right. As your driver, I attended all of them, and even though I don't know anything about science, I could give the conference in your place."<br>
 
"That's a great idea!" says Einstein. "Let's switch places then!"<br>
 
So they switch clothes and as soon as they arrive, the driver dressed as Einstein goes on stage and starts giving the usual speech, while the real Einstein, dressed as the car driver, attends it.<br>
 
But in the crowd, there is one scientist who wants to impress everyone and thinks of a very difficult question to ask Einstein, hoping he won't be able to respond. So this guy stands up and interrupts the conference by posing his very difficult question. The whole room goes silent, holding their breath, waiting for the response.<br>
 
The driver looks at him, dead in the eye, and says :<br><br>


 "Sir, your question is so easy to answer that I'm going to let my driver reply to it for me.</p><br>
 
 <a href="../index.jsp">Home</a>
</body>
</html>