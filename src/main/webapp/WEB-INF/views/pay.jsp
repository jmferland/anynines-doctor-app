<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    			$(button).addClass("btn btn-lg btn-primary");
    		});
    	})
    	$(function(){
    		$("select.expiryMonthSelectBox").prepend("<option value=''>Month</option>").val('');
    		$("select.expiryYearSelectBox").prepend("<option value=''>Year</option>").val('');
    	});
    	$(function(){
    		$(".cvvInput").append(" <div class='cvvBrandHelp'><a href='javascript:learnMoreCVV()'>Where do I find the verification number?</a></div>");
    		$(".brandSelectBox").change(function(){
    			var selectedBrand = $('select.brandSelectBox').val();
    			var helpExists = cvvHelpBrands[selectedBrand] !== undefined;
    			var $cvvBrandHelp = $(".cvvBrandHelp");
    			(helpExists ? $cvvBrandHelp.show() : $cvvBrandHelp.hide());
    		});
    		
    		cvvHelpBrands = {
    	    		'MASTER': 'mc',
    	    		'AMEX': 'amex',
    	    		'VISA': 'visa',
    	    		'MAESTRO': 'maestro'
    	    	};
    		
    		learnMoreCVV = function(){
        		var selectedBrand = $('select.brandSelectBox').val();
        		var helpBrand = cvvHelpBrands[selectedBrand];
        		var url = 'https://test.ctpe.net/frontend/html/' + helpBrand + '_cvd_en.html';
        		var width = 250, height = 350;
    		    if (selectedBrand == "MAESTRO"){ width = 400, height = 450; }
        		openPopup(url, width, height, 'no');
        	}
        	
    		openPopup = function(url, popW, popH, sb){
    			w = screen.availWidth;
    			h = screen.availHeight;

    			var leftPos = (w-popW)/2+10, topPos = (h-popH)/2+50;
    			var win = window.open(url,'POPUP',"scrollbars=yes,status=no,resizable=no,width=" + popW + ",height=" + popH + ",top=" + topPos + ",left=" + leftPos);
                win.focus();
    		}
    	});
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
			
			<dt>Doctor</dt>
			<dd>${bill.merchant.name}</dd>
			
			<dt>Description</dt>
			<dd>${bill.descriptor}</dd>
			
			<dt>Amount</dt>
			<dd>${bill.amount} ${bill.currency}</dd>
			
			<dt>Date Sent</dt>
			<dd><fmt:formatDate value="${bill.creationDate}" pattern="yyyy-MM-dd" /></dd>
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
					<form style="margin:0; padding:0;" action="https://test.ctpe.net/frontend/ExecutePayment;jsessionid=${item.token}">
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
	
		<c:if test="${not empty registrationTokens}">
			or <button id="clickToPay">Pay with a New Account</button>
			<script type="text/javascript">
		    	$(function(){
					$("#payWithNewAccount").hide();
		    		$("#clickToPay").click(function(){
		    			$("#clickToPay").hide();
		    			$("#payWithNewAccount").slideDown();
		    			$('html, body').animate({
    				        scrollTop: $("#payWithNewAccount").offset().top
    				    }, 2000);
		    		});
		    	})
			</script>
		</c:if>
		<div id="payWithNewAccount">
			<h3>Pay With a New Account</h3>
		    <form action="${redirectUrl}" id="${token}">
		        VISA AMEX MASTER
		    </form>
	    </div>
    </div>
</body>
</html>