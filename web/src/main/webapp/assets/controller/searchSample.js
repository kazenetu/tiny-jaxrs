var myApp = angular.module('searchSample', ['webApiService', 'userService']);

myApp.controller('searchSample', ['$scope', '$location', 'webApiService', 'userService',
    function ($scope, $location, webApiService, userService) {

        $scope.search = function () {

            webApiService.query('api/user/list?userId=:id', { id: userService.getId() },
                function (response) {
                	$scope.searchResult = response;
                 });

        }

        $scope.userId = userService.getId();
        $scope.userName = userService.getName();
    }
]);
