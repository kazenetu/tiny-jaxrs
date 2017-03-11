function PasswordChangeController($location, webApiService, userService) {
    var ctrl = this;

    ctrl.isError = false;
    ctrl.errorMsg = "";

    ctrl.passwordChange = function() {
        webApiService.post('api/user/passwordChange', {
            id : userService.getId(),
            password : ctrl.password,
            newPassword : ctrl.newPassword
        }, function(response) {
            if (response.result !== "OK") {
                ctrl.errorMsg = "現在のパスワードが異なります";
                ctrl.isError = true;
            } else {
                $location.path('/main');
            }
        });
    }
}

angular.module('App').controller('passwordChangeController', PasswordChangeController);
