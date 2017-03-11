function PasswordChangeController($scope, $location, webApiService, userService) {
    $scope.isError = false;
    $scope.errorMsg = "";

    $scope.passwordChange = function() {
        webApiService.post('api/user/passwordChange', {
            id : userService.getId(),
            password : $scope.password,
            newPassword : $scope.newPassword
        }, function(response) {
            if (response.result !== "OK") {
                $scope.errorMsg = "現在のパスワードが異なります";
                $scope.isError = true;
            } else {
                $location.path('/main');
            }
        });
    }
}

angular.module('App').controller('passwordChangeController', PasswordChangeController);
