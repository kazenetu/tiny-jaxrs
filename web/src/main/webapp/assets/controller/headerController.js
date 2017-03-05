var myApp = angular.module('headerController', ["userService"]);

myApp.controller('headerController', ['$scope','$location', 'userService',
    function($scope,$location, userService){
        $scope.userName = function() {
            return userService.getName();
        };

        $scope.passwordChange = function() {
            $location.path('/passwordChange');
        };
    }
]);
