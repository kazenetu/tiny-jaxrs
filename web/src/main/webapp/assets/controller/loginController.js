front.controller.LoginController = function LoginController($location, webApiService, userService) {
    var ctrl = this;

    ctrl.id = "";
    ctrl.password = "";
    ctrl.isError = false;
    ctrl.errorMsg = "";

    ctrl.login = function() {

        webApiService.post('api/user/login', {
            id : ctrl.userId,
            password : ctrl.password
        }, function(response) {
            if (response.result !== "OK") {
                ctrl.errorMsg = "ログインできませんでした";
                ctrl.isError = true;
            } else {
                ctrl.isError = false;
                userService.setId(ctrl.userId);
                userService.setName(response.name);
                $location.path('/main');
            }
        });
    }
}

angular.module('App').controller('loginController', front.controller.LoginController);
