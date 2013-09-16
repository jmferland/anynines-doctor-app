/***
 * Controller to handle interfacing with the RESTful endpoint
 */
$.ajaxSetup({
    cache:false
});

var utils = {
    _url:'',
    setup:function (u) {
        this._url = u;
    },
    url:function (u) {
        return this._url + u;
    },
    get:function (url, data, cb) {
        $.ajax({
            type:'GET',
            url:url,
            cache:false,
            dataType:'json',
            contentType:'application/json; charset=utf-8',
            success:cb,
            error:function () {
                alert('error trying to retrieve ' + u);
            }
        });
    },
    put:function (url, data, cb) {
        var k = '_method',
            v = 'PUT';
        data[k] = v;
        var headers = {};
        headers[k] = v;
        $.ajax({
            type:'POST',
            url:url,
            cache:false,
            headers:headers,
            data:data,
            success:function (result) {
                cb(result);
            },
            error:function (e) {
                console.log('error PUT\'ing to url ' + url + '. ' + JSON.stringify(e));
            }
        });     // todo

    },
    post:function (u, data, cb) {
        $.ajax({
            type:'POST',
            url:u,
            cache:false,
            dataType:'json',
            data:data,
            contentType:'application/json; charset=utf-8',
            success:cb,
            error:function () {
                alert('error trying to post to ' + u);
            }
        });
    }
};

function CustomerCtrl($scope, $http) {
    $scope.customers = [];

    $http({
	        method: 'GET',
	        url: '/crm/customers',
	        data: {}
	    }).success(function (result) {
	    $scope.customers = result;
	});

    $scope.query = '';

    $scope.searchResultsFound = function () {
        return $scope.customers != null && $scope.customers.length > 0;
    };

    $scope.load = function (customer) {
        $scope.customer = customer;
        $scope.id = customer.id;
    };

    $scope.search = function () {
        var u = utils.url('/crm/search/customers?q=' + $scope.query);
        utils.get(u, {}, function (customers) {
            $scope.$apply(function () {
                $scope.customers = customers;
                if ($scope.searchResultsFound()) {
                    if (customers.length == 1) {
                        $scope.load(customers[0]);
                    }
                }
            });
        });
    };
    $scope.isCustomerLoaded = function () {
        return $scope.customer != null && $scope.customer.id != null && $scope.customer.id > 0;
    };

    function loadCustomerById(id, cb) {
        var u = utils.url('/crm/customers/' + id);
        utils.get(u, {}, cb);
    }

    $scope.lookupCustomer = function () {
        loadCustomerById($scope.id, function (c) {
            $scope.$apply(function () {
                $scope.load(c);
            });
        });
    };

    $scope.save = function () {
        var id = $scope.id;
        var data = {   firstName:$scope.customer.firstName, lastName:$scope.customer.lastName  };

        function exists(o, p, cb) {
            if (o[p] && o[p] != null) {
                cb(p, o[p]);
            }
        }

        exists($scope.customer, 'id', function (pName, val) {
            data[pName] = val;
        });
        exists($scope.customer, 'signupDate', function (pN, v) {
            data[pN] = v;
        });
        var idReceivingCallback = function (id) {
            console.log('id is ' + id);
            $scope.$apply(function () {
                $scope.id = id;
                $scope.lookupCustomer();
            });

        };

        var u = null;
        if (id != null && id > 0) {
            // then we're simply going to update it
            u = utils.url('/crm/customers/' + id);
            console.log('JSON to send' + JSON.stringify(data))
            utils.post(u, JSON.stringify(data), idReceivingCallback);
        }
        else {
            u = utils.url('/crm/customers');
            utils.put(u, data, idReceivingCallback);
        }

    };

    $scope.trash = function () {
        $scope.id = null;
        $scope.customer = null;
    };
}

function MerchantCtrl($scope, $http) {
    $scope.merchants = [];

    $http({
	        method: 'GET',
	        url: '/crm/merchants',
	        data: {}
	    }).success(function (result) {
	    $scope.merchants = result;
	});

    $scope.query = '';

    $scope.searchResultsFound = function () {
        return $scope.merchants != null && $scope.merchants.length > 0;
    };

    $scope.load = function (merchant) {
        $scope.merchant = merchant;
        $scope.id = merchant.id;
    };

    $scope.search = function () {
        var u = utils.url('/crm/search/merchants?q=' + $scope.query);
        utils.get(u, {}, function (merchants) {
            $scope.$apply(function () {
                $scope.merchants = merchants;
                if ($scope.searchResultsFound()) {
                    if (merchants.length == 1) {
                        $scope.load(merchants[0]);
                    }
                }
            });
        });
    };
    $scope.isMerchantLoaded = function () {
        return $scope.merchant != null && $scope.merchant.id != null && $scope.merchant.id > 0;
    };

    function loadMerchantById(id, cb) {
        var u = utils.url('/crm/merchants/' + id);
        utils.get(u, {}, cb);
    }

    $scope.lookupMerchant = function () {
        loadMerchantById($scope.id, function (c) {
            $scope.$apply(function () {
                $scope.load(c);
            });
        });
    };

    $scope.save = function () {
        var id = $scope.id;
        var data = {
        		name: $scope.merchant.name,
        		securitySender: $scope.merchant.securitySender,
        		userLogin: $scope.merchant.userLogin,
        		userPassword: $scope.merchant.userPassword,
        		channelId: $scope.merchant.channelId
        	};

        function exists(o, p, cb) {
            if (o[p] && o[p] != null) {
                cb(p, o[p]);
            }
        }

        exists($scope.merchant, 'id', function (pName, val) {
            data[pName] = val;
        });
        exists($scope.merchant, 'creationDate', function (pN, v) {
            data[pN] = v;
        });
        var idReceivingCallback = function (id) {
            console.log('id is ' + id);
            $scope.$apply(function () {
                $scope.id = id;
                $scope.lookupMerchant();
            });

        };

        var u = null;
        if (id != null && id > 0) {
            // then we're simply going to update it
            u = utils.url('/crm/merchants/' + id);
            console.log('JSON to send' + JSON.stringify(data))
            utils.post(u, JSON.stringify(data), idReceivingCallback);
        }
        else {
            u = utils.url('/crm/merchants');
            utils.put(u, data, idReceivingCallback);
        }

    };

    $scope.trash = function () {
        $scope.id = null;
        $scope.merchant = null;
    };
}

function BillCtrl($scope, $http) {
    $scope.bills = [];
    
    $http({
	        method: 'GET',
	        url: '/crm/bills',
	        data: {}
	    }).success(function (result) {
	    $scope.bills = result;
	});

    $scope.query = '';
    
    $scope.currencies = [
	    { value: 'EUR', name: 'EUR'},
	    { value: 'USD', name: 'USD'},
	    { value: 'CAD', name: 'CAD'}
    ];
              
    $scope.merchants = [];
    
    $http({
	        method: 'GET',
	        url: '/crm/merchants',
	        data: {}
	    }).success(function (result) {
	    $scope.merchants = result;
	});
    
    $scope.customers = [];
    
    $http({
	        method: 'GET',
	        url: '/crm/customers',
	        data: {}
	    }).success(function (result) {
	    $scope.customers = result;
	});

    $scope.searchResultsFound = function () {
        return $scope.bills != null && $scope.bills.length > 0;
    };

    $scope.load = function (bill) {
        $scope.bill = bill;
        $scope.id = bill.id;
        
        $('#qrcode').empty();
        var url ="https://" + location.host + "/pay/" + bill.token;
        console.log('generating qr code for url: ' + url);
        var qrcode = new QRCode(document.getElementById("qrcode"), url);
        $('#qrcode').parent().attr('href', url);
    };

    $scope.search = function () {
        var u = utils.url('/crm/search/bills?q=' + $scope.query);
        utils.get(u, {}, function (bills) {
            $scope.$apply(function () {
                $scope.bills = bills;
                if ($scope.searchResultsFound()) {
                    if (bills.length == 1) {
                        $scope.load(bills[0]);
                    }
                }
            });
        });
    };
    $scope.isBillLoaded = function () {
        return $scope.bill != null && $scope.bill.id != null && $scope.bill.id > 0;
    };

    function loadBillById(id, cb) {
        var u = utils.url('/crm/bills/' + id);
        utils.get(u, {}, cb);
    }

    $scope.lookupBill = function () {
        loadBillById($scope.id, function (c) {
            $scope.$apply(function () {
                $scope.load(c);
            });
        });
    };

    $scope.save = function () {
        var id = $scope.id;
        var data = {
        		merchantId: $scope.bill.merchant.id,
        		customerId: $scope.bill.customer.id,
        		token: $scope.bill.token,
        		descriptor: $scope.bill.descriptor,
        		amount: $scope.bill.amount,
        		currency: $scope.bill.currency
        	};

        function exists(o, p, cb) {
            if (o[p] && o[p] != null) {
                cb(p, o[p]);
            }
        }

        exists($scope.bill, 'id', function (pName, val) {
            data[pName] = val;
        });
        exists($scope.bill, 'token', function (pName, val) {
            data[pName] = val;
        });
        exists($scope.bill, 'creationDate', function (pN, v) {
            data[pN] = v;
        });
        var idReceivingCallback = function (id) {
            console.log('id is ' + id);
            $scope.$apply(function () {
                $scope.id = id;
                $scope.lookupBill();
            });

        };

        var u = null;
        if (id != null && id > 0) {
            // then we're simply going to update it
            u = utils.url('/crm/bills/' + id);
            console.log('JSON to send' + JSON.stringify(data))
            utils.post(u, data, idReceivingCallback);
        }
        else {
            u = utils.url('/crm/bills');
            utils.put(u, data, idReceivingCallback);
        }

    };

    $scope.trash = function () {
        $scope.id = null;
        $scope.bill = null;
    };
}

function RegistrationCtrl($scope, $http) {
    $scope.registrations = [];
    
    $http({
	        method: 'GET',
	        url: '/crm/registrations',
	        data: {}
	    }).success(function (result) {
	    $scope.registrations = result;
	});

    $scope.query = '';

    $scope.customers = [];
    
    $http({
	        method: 'GET',
	        url: '/crm/customers',
	        data: {}
	    }).success(function (result) {
	    $scope.customers = result;
	});

    $scope.searchResultsFound = function () {
        return $scope.registrations != null && $scope.registrations.length > 0;
    };

    $scope.load = function (registration) {
        $scope.registration = registration;
        $scope.id = registration.id;
    };

    $scope.search = function () {
        var u = utils.url('/crm/search/registrations?q=' + $scope.query);
        utils.get(u, {}, function (registrations) {
            $scope.$apply(function () {
                $scope.registrations = registrations;
                if ($scope.searchResultsFound()) {
                    if (registrations.length == 1) {
                        $scope.load(registrations[0]);
                    }
                }
            });
        });
    };
    $scope.isRegistrationLoaded = function () {
        return $scope.registration != null && $scope.registration.id != null && $scope.registration.id > 0;
    };

    function loadRegistrationById(id, cb) {
        var u = utils.url('/crm/registrations/' + id);
        utils.get(u, {}, cb);
    }

    $scope.lookupRegistration = function () {
        loadRegistrationById($scope.id, function (c) {
            $scope.$apply(function () {
                $scope.load(c);
            });
        });
    };

    $scope.save = function () {
        var id = $scope.id;
        var data = {
        		customerId: $scope.registration.customer.id,
        		code: $scope.registration.code,
        		brand: $scope.registration.brand,
        		bin: $scope.registration.bin,
        		last4Digits: $scope.registration.last4Digits
        	};

        function exists(o, p, cb) {
            if (o[p] && o[p] != null) {
                cb(p, o[p]);
            }
        }

        exists($scope.registration, 'id', function (pName, val) {
            data[pName] = val;
        });
        exists($scope.registration, 'creationDate', function (pN, v) {
            data[pN] = v;
        });
        var idReceivingCallback = function (id) {
            console.log('id is ' + id);
            $scope.$apply(function () {
                $scope.id = id;
                $scope.lookupRegistration();
            });

        };

        var u = null;
        if (id != null && id > 0) {
            // then we're simply going to update it
            u = utils.url('/crm/registrations/' + id);
            console.log('JSON to send' + JSON.stringify(data))
            utils.post(u, data, idReceivingCallback);
        }
        else {
            u = utils.url('/crm/registrations');
            utils.put(u, data, idReceivingCallback);
        }

    };

    $scope.trash = function () {
        $scope.id = null;
        $scope.registration = null;
    };
}