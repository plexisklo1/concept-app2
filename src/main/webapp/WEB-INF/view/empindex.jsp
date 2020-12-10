<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<title>Employee/Team list</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
</head>
<body>

	<div class="container text-center mx-auto">
		<h3 class="text-center">Employees</h3>
		<table class="table">
		<thead class="thead-light">
			<tr>
				<th>#</th>
				<th>First name</th>
				<th>Last name</th>
				<th>Team</th>
				<th>Action</th>
			</tr>
		</thead>
		
			<c:forEach var="emp" items="${employees}">
				<tr class="border-dark">
					<td>${emp.id}</td>
					<td>${emp.firstName}</td>
					<td>${emp.lastName}</td>
					<td>${emp.team.name}</td>
					<c:url var="empdetail" value="/empdetail">
						<c:param name="empid" value="${emp.id}" />
					</c:url>
					<c:url var="empedit" value="/empedit">
						<c:param name="empid" value="${emp.id}" />
					</c:url>
					<c:url var="empremove" value="/empremove">
						<c:param name="empid" value="${emp.id}" />
					</c:url>
					<td><a href="${empdetail}" class="btn btn-info">Details</a> 
					<a href="${empedit}" class="btn btn-dark">Update</a> 
					<security:authorize access="hasRole('ADMIN')"><a href="${empremove}" class="btn btn-warning" onclick="if(!(confirm('Delete subject?'))) return false">Remove</a></security:authorize></td>
				</tr>
			</c:forEach>
		</table>
		<br> <security:authorize access='hasAnyRole("EXEC","ADMIN")'><a
			href="${pageContext.request.contextPath}/addemployee" class="btn btn-secondary">New	employee</a></security:authorize> <br> <br>
		<hr>
		<br>
		<br>
		<h3>Teams</h3>
		<table class="table">
		<thead class="thead-light">
			<tr>
				<th>#</th>
				<th>Name</th>
				<th>Description</th>
				<security:authorize access='hasAnyRole("EXEC","ADMIN")'><th>Action</th></security:authorize>
			</tr>
		</thead>
			<c:forEach var="team" items="${teams}">
				<c:url var="teamedit" value="/teamedit">
					<c:param name="teamid" value="${team.id}" />
				</c:url>
				<c:url var="teamRemoveURL" value="/removeTeam">
					<c:param name="teamId" value="${team.id}"/>
				</c:url>
				<tr>
					<td>${team.id}</td>
					<td>${team.name}</td>
					<td>${team.description}</td>
					<security:authorize access='hasAnyRole("EXEC","ADMIN")'><td><a href="${teamedit}" class="btn btn-dark">Update</a> <security:authorize access="hasRole('ADMIN')"><a href="${teamRemoveURL}" class="btn btn-warning" 
					onclick="if(!(confirm('Remove team?\nTeam members will be Unassigned.'))) {return false;}">Remove</a></security:authorize></td></security:authorize>
				</tr>
			</c:forEach>
		</table>
		<br> <security:authorize access='hasAnyRole("EXEC","ADMIN")'><a href="${pageContext.request.contextPath}/teamcreate" class="btn btn-secondary">New
			team</a></security:authorize>
	</div><br>
	<p>Logged in as: <security:authentication property="principal.username" /></p>
<form:form action="${pageContext.request.contextPath}/login" method="POST">
	<input type="submit" value="Logout"/>
</form:form>

</body>
</html>
