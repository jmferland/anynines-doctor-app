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
                                <a ng-click="load(bill)">{{bill.token}} {{bill.descriptor}}</a> </span>
                        </div>

                    </div>
                </div>
            </form>
    </div>
 
    <div>
    	<a href="/admin/customers">Customers</a>
    	<a href="/admin/merchants">Merchants</a>
    	<a href="/admin/bills">Bills</a>
    	<a href="/admin/registrations">Registrations</a>
    </div>

    <form class="form-horizontal" ng-submit="updateBill">
        <fieldset>
            <legend>
                <span class="display-visible-{{!isBillLoaded()}}"> Create New Bill </span>
                <span class="display-visible-{{!!isBillLoaded()}}"> Update {{bill.token}} {{bill.descriptor}} - <span>#</span>{{bill.id}} </span>
            </legend>
            <div class="control-group display-visible-{{isBillLoaded()}}">
                <label class="control-label" for="billDescriptor">Payment Link:</label>

                <div class="controls">
                    <div id="qrcode"></div>

                    <p class="help-block">Scan the QR Code or click link to pay</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="billMerchantId">Merchant:</label>

                <div class="controls">
                	<select id="billMerchantId"
                		ng-model="bill.merchantId" ng-options="merchant.id as merchant.name for merchant in merchants"></select>

                    <p class="help-block">Change the merchant</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="billCustomerId">Customer:</label>

                <div class="controls">
                	<select id="billCustomerId"
                		ng-model="bill.customerId" ng-options="customer.id as customer.id for customer in customers"></select>

                    <p class="help-block">Change the customer</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="billDescriptor">Descriptor:</label>

                <div class="controls">
                    <input class="input-xlarge" id="billDescriptor" type="text" ng-model="bill.descriptor"
                           placeholder="descriptor" required="required"/>

                    <p class="help-block">Change the descriptor</p>
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
</body>
</html>