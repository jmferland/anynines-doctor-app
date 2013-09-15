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

<div ng-controller="MerchantCtrl">

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

                        <div ng-repeat="merchant in merchants" class="search-results">
                            <span class="title">
                                <span style="font-size: smaller"><span>#</span>{{merchant.id}}</span>
                                <a ng-click="load(merchant)">{{merchant.name}}</a> </span>
                        </div>

                    </div>
                </div>
            </form>
    </div>
 
    <ul class="navi">
    	<li><a href="/admin/customers">Customers</a></li>
    	<li><a href="/admin/merchants">Merchants</a></li>
    	<li><a href="/admin/bills">Bills</a></li>
    	<li><a href="/admin/registrations">Registrations</a></li>
    </ul>

    <form class="form-horizontal" ng-submit="updateMerchant">
        <fieldset>
            <legend>
                <span class="display-visible-{{!isMerchantLoaded()}}"> Create New Merchant </span>
                <span class="display-visible-{{!!isMerchantLoaded()}}"> Update {{merchant.name}} - <span>#</span>{{merchant.id}} </span>
            </legend>
            <div class="control-group">
                <label class="control-label" for="merchantName">Name:</label>

                <div class="controls">
                    <input class="input-xlarge" id="merchantName" type="text" ng-model="merchant.name"
                           placeholder="name" required="required"/>

                    <p class="help-block">Change the name</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="merchantSecuritySender">Security Sender:</label>

                <div class="controls">
                    <input class="input-xlarge" id="merchantSecuritySender" type="text" ng-model="merchant.securitySender"
                           placeholder="security sender" required="required"/>

                    <p class="help-block">Change the security sender</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="merchantUserLogin">User Login:</label>

                <div class="controls">
                    <input class="input-xlarge" id="merchantUserLogin" type="text" ng-model="merchant.userLogin"
                           placeholder="user login" required="required"/>

                    <p class="help-block">Change the user login</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="merchantUserPassword">User Password:</label>

                <div class="controls">
                    <input class="input-xlarge" id="merchantUserPassword" type="text" ng-model="merchant.userPassword"
                           placeholder="user password" required="required"/>

                    <p class="help-block">Change the user password</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="merchantChannelId">Channel Id:</label>

                <div class="controls">
                    <input class="input-xlarge" id="merchantChannelId" type="text" ng-model="merchant.channelId"
                           placeholder="channel id" required="required"/>

                    <p class="help-block">Change the channel Id</p>
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