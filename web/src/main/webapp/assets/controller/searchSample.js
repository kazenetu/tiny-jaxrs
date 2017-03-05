var myApp = angular.module('searchSample', ['webApiService', 'userService']);

myApp.controller('searchSample', ['$scope', '$location', 'webApiService', 'userService',
    function ($scope, $location, webApiService, userService) {

        $scope.search = function () {

            var result = webApiService.query('api/user/list?userId=:id', { id: userService.getId() });
            result.$promise.then(function (response) {
                $scope.searchResult = response;
            }, function (response) {
                $location.path('/');
            });
        }

        $scope.userId = userService.getId();
        $scope.userName = userService.getName();
    }
]);
