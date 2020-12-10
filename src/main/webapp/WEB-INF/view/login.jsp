<%@ page isELIgnored='false'%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
</head>
<body>

	<div class="container mx-auto text-center my-3 w-25">
		<table class="table border border-info rounded-pill">
			<form:form method="POST"
				action="${pageContext.request.contextPath}/authenticate">
				<c:if test="${param.error!=null}">
					<tr><td colspan="2" class="border-top-0"><div class="alert alert-danger" role="alert">
						<span class="glyphicon glyphicon-exclamation-sign"
							aria-hidden="true"></span> <span class="sr-only">Error:</span>
						Enter a valid username/password.
					</div></td></tr>
					
				</c:if>
				<c:if test="${param.logout!=null}">
					<div class="alert alert-success" role="alert">
						<span class="glyphicon glyphicon-exclamation-sign"
							aria-hidden="true"></span> <span class="sr-only">User:</span> You
						have logged out.
					</div>
				</c:if>
				<tr>
					<td class="border-top-0"><label for="username"
						class="col-sm-2 control-label text-center mt-2">Username:</label></td>
					<td class="border-top-0"><input type="text" name="username" id="username"
						placeholder="Username" required class="form-control" /></td>
				</tr>
				<tr>
					<td class="border-top-0"><label for="password"
						class="col-sm-2 control-label text-center mt-2">Password:</label></td>
					<td class="border-top-0"><input type="password" name="password" id="password"
						placeholder="Password" required class="form-control" /></td>
				</tr>
				<tr class="container text-center mx-auto">
					<td colspan="2" class="border-top-0"><input type="submit" value="Login"
						class="btn btn-info btn-block " /></td>
				</tr>

			</form:form>
		</table>
	</div>
</body>
</html>