var myApp = angular.module('App', [ 'ngResource', 'ngRoute', 'webApiService','storageService','userService' ]);

myApp.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl : 'view/login.html'
    }).when('/passwordChange', {
        templateUrl : 'view/passwordChange.html',
    }).when('/main', {
        templateUrl : 'view/main.html',
    }).when('/userlist', {
        templateUrl : 'view/master/userList.html',
    }).when('/useredit', {
        templateUrl : 'view/master/userEdit.html',
    }).otherwise({
        redirectTo : '/'
    });

    // ルートURL退避
    sessionStorage.setItem("rootURL",location.protocol + '//' + location.host + '/web/');
}
]);