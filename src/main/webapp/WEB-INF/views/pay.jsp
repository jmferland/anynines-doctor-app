<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
	<meta content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <script src="https://test.ctpe.net/frontend/widget/v3/widget.js?compressed=false&language=en&style=none" ></script>
    <script type="text/javascript">
    	$ = cnp_jQuery;
    	$(function() {
    		$("form.cnpForm input.cardHolderInputField").each(function(i, input) {
				$(input).val('${bill.customer.firstName} ${bill.customer.lastName}')
			});
    	})
    	$(function(){
    		$("form.cnpForm .cardSubmitButton").each(function(i, button){
    			$(button).addClass("btn btn-primary");
    		});
    	})
    </script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/web/assets/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/web/assets/css/style.css">
</head>
<body>
	<div style="padding: 10px; max-width: 520px">
		<img class="logo" src="${pageContext.request.contextPath}/web/assets/img/medical-payments.png" alt="Medical Payments"/>
		
		<h2>Bill Payment</h2>
		<dl class="dl-horizontal">
			<dt>Patient</dt>
			<dd>${bill.customer.firstName} ${bill.customer.lastName}</dd>
			
			<dt>Descriptor</dt>
			<dd>${bill.descriptor}</dd>
			
			<dt>Amount</dt>
			<dd>${bill.amount} ${bill.currency}</dd>
		</dl>
		
		<c:if test="${not empty registrationTokens}">
		<h3>Pay With a Registered Account</h3>
		<table class="table table-striped table-hover">
			<tr>
				<th>Brand</th>
				<th>Account</th>
				<th></th>
			</tr>
			<c:forEach items="${registrationTokens}" var="item">
			<tr>
				<td>${item.registration.brand}</td>
				<td>************${item.registration.last4Digits}</td>
				<td>
					<form action="https://test.ctpe.net/frontend/ExecutePayment;jsessionid=${item.token}">
						<!-- TODO: change redirection URL -->
						<input type="hidden" name="FRONTEND.VERSION" value="3">
						<input type="hidden" name="FRONTEND.MODE" value="ASYNC">
						<input type="hidden" name="FRONTEND.RESPONSE_URL" value="${redirectUrl}">
						<button type="submit" class="btn btn-sm btn-primary">Pay</button>
					</form>
				</td>
			</tr>
			</c:forEach>
		</table>
		</c:if>
	
		<h3>Pay With a New Account</h3>
	    <form action="${redirectUrl}" id="${token}">
	        VISA AMEX MASTER
	    </form>
    </div>
</body>
</html>