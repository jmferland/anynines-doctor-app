<%@ page session="false" %>
<!doctype html>
<html ng-app>
<head>

    <script src="${pageContext.request.contextPath}/web/assets/js/jquery-1.7.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/web/assets/js/angular-1.0.0rc6.js"></script>
    <script src="${pageContext.request.contextPath}/web/assets/js/qrcode.min.js"></script>
    <script src="${pageContext.request.contextPath}/web/views/controller.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/web/assets/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/web/views/style.css"/>

</head>
<body>
<script language="javascript" type="text/javascript">
    <!--
    $(function () {
        utils.setup('${pageContext.request.contextPath}');
    });

    //-->
</script>

<div ng-controller="BillCtrl">

    <div style=" z-index: -21; float: left;padding-top :10px; padding:10px;min-width: 250px;max-width:300px;  ">
        <form class="well form-search" ng-submit="search()">
                <div>
                    <input type="text" id="search" class="input-medium search-query" ng-model="query"/>
                    <a href="#" class="icon-search" ng-click="search()"></a>
                </div>
                <div style="padding-top: 10px;">
                    <div ng-show=" !searchResultsFound()">
                        <span class="no-records">(no results)</span>
                    </div>

                    <div ng-show=" searchResultsFound()">

                        <div ng-repeat="bill in bills" class="search-results">
                            <span class="title">
                                <span style="font-size: smaller"><span>#</span>{{bill.id}}</span>
                                <a ng-click="load(bill)">{{bill.descriptor}}</a> </span>
                        </div>

                    </div>
                </div>
            </form>
    </div>
 
 	<div class="pull-left" style="padding-top: 10px;">
	    <ul class="nav nav-tabs">
	    	<li><a href="/admin/customers">Patients</a></li>
	    	<li><a href="/admin/merchants">Doctors</a></li>
	    	<li class="active"><a href="/admin/bills">Bills</a></li>
	    	<li><a href="/admin/registrations">Registrations</a></li>
	    </ul>
	
	    <form class="form-horizontal" ng-submit="updateBill">
	        <fieldset>
	            <legend>
	                <span class="display-visible-{{!isBillLoaded()}}"> Create New Bill </span>
	                <span class="display-visible-{{!!isBillLoaded()}}"> Update {{bill.descriptor}} - <span>#</span>{{bill.id}} </span>
	            </legend>
	            <div class="control-group display-visible-{{isBillLoaded()}}">
	                <label class="control-label" for="billDescriptor">Payment Link:</label>
	
	                <div class="controls">
	                    <a href=""><div id="qrcode"></div></a>
	
	                    <p class="help-block">Scan or click the QR Code to pay</p>
	                    <br />
	                </div>
	            </div>
	            <div class="control-group">
	                <label class="control-label" for="billMerchantId">Doctor:</label>
	
	                <div class="controls">
	                	<select id="billMerchantId" required="required"
	                		ng-model="bill.merchant.id" ng-options="merchant.id as merchant.name for merchant in merchants"></select>
	
	                    <p class="help-block">Change the doctor</p>
	                </div>
	            </div>
	            <div class="control-group">
	                <label class="control-label" for="billCustomerId">Patient:</label>
	
	                <div class="controls">
	                	<select id="billCustomerId" required="required"
	                		ng-model="bill.customer.id" ng-options="customer.id as customer.id for customer in customers"></select>
	
	                    <p class="help-block">Change the patient</p>
	                </div>
	            </div>
	            <div class="control-group">
	                <label class="control-label" for="billDescriptor">Description:</label>
	
	                <div class="controls">
	                    <input class="input-xlarge" id="billDescriptor" type="text" ng-model="bill.descriptor"
	                           placeholder="descriptor" required="required"/>
	
	                    <p class="help-block">Change the description</p>
	                </div>
	            </div>
	            <div class="control-group">
	                <label class="control-label" for="billAmount">Amount:</label>
	
	                <div class="controls">
	                    <input class="input-xlarge" id="billAmount" type="text" ng-model="bill.amount"
	                           placeholder="amount" required="required"/>
	
	                    <p class="help-block">Change the amount</p>
	                </div>
	            </div>
	            <div class="control-group">
	                <label class="control-label" for="billCurrency">Currency:</label>
	
	                <div class="controls">
	                	<select id="billCurrency"
	                		ng-model="bill.currency" ng-options="currency.value as currency.name for currency in currencies"></select>
	
	                    <p class="help-block">Change the currency</p>
	                </div>
	            </div>
	
	            <div class="form-actions">
	                <button type="submit" class="btn btn-primary" ng-click="save()" ng-model-instant>
	                    <a class="icon-plus"></a> Save
	                </button>
	                <button class="btn " ng-click="trash()"><a class="icon-trash"></a> Cancel</button>
	            </div>
	        </fieldset>
	    </form>
	</div>
</div>
</body>
</html>