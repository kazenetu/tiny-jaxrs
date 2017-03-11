function HeaderController($scope, $location, userService) {
    $scope.userName = function() {
        return userService.getName();
    };

    $scope.passwordChange = function() {
        $location.path('/passwordChange');
    };
}

angular.module('App').controller('headerController', HeaderController);
