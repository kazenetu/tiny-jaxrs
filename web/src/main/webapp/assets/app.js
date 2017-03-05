var myApp = angular.module('App', [ 'ngResource', 'ngRoute', 'headerController', 'loginController','passwordChangeController', 'searchSample' ]);

myApp.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl : 'view/login.html'
    }).when('/passwordChange', {
        templateUrl : 'view/passwordChange.html',
    }).when('/main', {
        templateUrl : 'view/main.html',
    }).otherwise({
        redirectTo : '/'
    });

    // ルートURL退避
    sessionStorage.setItem("rootURL",location.protocol + '//' + location.host + '/web/');
}
]);