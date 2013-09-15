<%@ page session="false" %>
<!doctype html>
<html ng-app>
<head>

    <script src="${pageContext.request.contextPath}/web/assets/js/jquery-1.7.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/web/assets/js/angular-1.0.0rc6.js"></script>
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
 

    <form class="form-horizontal" ng-submit="updateBill">
        <fieldset>
            <legend>
                <span class="bill-visible-{{!isBillLoaded()}}"> Create New Bill </span>
                <span class="bill-visible-{{isBillLoaded()}}"> Update {{bill.token}} {{bill.descriptor}} - <span>#</span>{{bill.id}} </span>
            </legend>
            <!-- TODO: display a read-only token and QR code after it has been saved!!! -->
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
                		ng-model="bill.currency" ng-options="x.id as x.name for x in array"></select>

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