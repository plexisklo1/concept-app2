<%@ page isELIgnored="false"%>

<!DOCTYPE html>
<html>
<head>
<title>Invalid request</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
</head>
<body>
	<br>
	<div class="container text-center mx-auto">
		<h4 class="">Invalid request</h4>
		<br>
		<br> ${error} <br> <br> 
		<a	href="${pageContext.request.contextPath}/employees"
			class="btn btn-secondary">Back</a>
	</div>
</body>
</html>