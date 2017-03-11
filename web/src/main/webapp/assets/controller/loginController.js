function LoginController($scope, $location, webApiService, userService) {
    $scope.id = "";
    $scope.password = "";
    $scope.isError = false;
    $scope.errorMsg = "";

    $scope.login = function() {

        webApiService.get('api/user/login?userId=:id&password=:password', {
            id : $scope.userId,
            password : $scope.password
        }, function(response) {
            if (response.result !== "OK") {
                $scope.errorMsg = "ログインできませんでした";
                $scope.isError = true;
            } else {
                $scope.isError = false;
                userService.setId($scope.userId);
                userService.setName(response.name);
                $location.path('/main');
            }
        });
    }
}

angular.module('App').controller('loginController', LoginController);
