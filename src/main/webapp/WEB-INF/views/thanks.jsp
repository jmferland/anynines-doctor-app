<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
	<meta content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <script src="${pageContext.request.contextPath}/web/assets/js/jquery-1.7.2.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/web/assets/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/web/views/style.css">
</head>
<body>
	<div style="padding: 10px; max-width: 520px">
		<img class="logo" src="${pageContext.request.contextPath}/web/assets/img/medical-payments.png" alt="Medical Payments"/>
		<p style="font-size: 110%;">Thank you for paying, ${bill.customer.firstName} ${bill.customer.lastName}. Hope you're better!</p>
		<c:if test="${not success}">
			<p>Unfortunately, your payment was <strong>unsuccessful</strong>.</p> <a href="/pay/${billToken}">Click here to try and pay again.</a>
		</c:if>
	</div>
</body>
</html>