<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
	<c:choose>
		<c:when test="${not empty current_user_id}">
			<c:out value="${current_user_fn} ${current_user_ln}"></c:out>
			<a href="logout">Logout</a>
		</c:when>
		<c:otherwise>Not logged in</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${empty blogEntry}">
      Nema unosa!
    </c:when>
		<c:otherwise>
			<h1>
				<c:out value="${blogEntry.title}" />
			</h1>
			<p>
				<c:out value="${blogEntry.text}" />
			</p>

			<c:if test="${user.id == current_user_id}">
				<a href="edit?id=${blogEntry.id}">EDIT</a>
			</c:if>

			<c:if test="${!blogEntry.comments.isEmpty()}">
				<ul>
					<c:forEach var="e" items="${blogEntry.comments}">
						<li><div style="font-weight: bold">
								[User=
								<c:out value="${e.usersEMail}" />
								]
								<c:out value="${e.postedOn}" />
							</div>
							<div style="padding-left: 10px;">
								<c:out value="${e.message}" />
							</div></li>
					</c:forEach>
				</ul>
			</c:if>

			<form
				action="${blogEntry.id}/comment<c:if test="${not empty current_user_id}">?id=${current_user_id}</c:if>"
				method="post">
				<textarea rows="3" cols="30" name="comment"></textarea>
				<br>
				<c:if test="${empty current_user_id}">
      	    Email: <input type="email" name="email">
				</c:if>
				<c:if test="${not empty comment_error}">
					<br>${comment_error}<br>
				</c:if>
				<input type="submit" value="Post"> <input type="reset">
			</form>

		</c:otherwise>
	</c:choose>

	<a href="servleti/main">Home</a>

</body>
</html>
