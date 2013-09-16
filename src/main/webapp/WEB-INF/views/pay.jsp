<%@ page session="false" %>
<!doctype html>
<html>
<head>
    <script src="https://test.ctpe.net/frontend/widget/v3/widget.js?compressed=false&language=en&style=card" ></script>
</head>
<body>
	<h1>Pay Bill</h1>
	<h2>Details</h2>
	<dl>
		<dt>Descriptor:</dt>
		<dd>${bill.descriptor}</dd>
		
		<dt>Amount:</dt>
		<dd>${bill.amount} ${bill.currency}</dd>
	</dl>
	
	<h2>Pay With Existing Account</h2>
	<c:forEach items="${tokenToRegistration}" var="item">
		<form action="https://test.ctpe.net/frontend/ExecutePayment;jsessionid=${item.key}">
			<!-- TODO: change redirection URL -->
			<input type="hidden" name="FRONTEND.VERSION" value="3">
			<input type="hidden" name="FRONTEND.MODE" value="ASYNC">
			<input type="hidden" name="FRONTEND.RESPONSE_URL" value="https://test.ctpe.net/frontend/Integrationguide/COPYandPAY_Thanks.html">
			<button type="submit">Pay with ${item.value.code}</button>
		</form>
	</c:forEach>

	<h2>Pay With a New Account</h2>
    <form action="" id="${token}">
        VISA AMEX MASTER
    </form>
</body>
</html>