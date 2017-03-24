front.controller.LoginController = function LoginController($location, webApiService, userService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('ログイン');

    var ctrl = this;

    ctrl.id = "";
    ctrl.password = "";

    ctrl.login = function() {

        webApiService.post('api/user/login', {
            id : ctrl.userId,
            password : ctrl.password
        }, function(response) {
            if (response.result !== "OK") {
                ctrl.showError('ログインできませんでした');
            } else {
                ctrl.hideError();
                userService.setId(ctrl.userId);
                userService.setName(response.name);
                $location.path('/main');
            }
        });
    }
}

angular.module('App').controller('loginController', front.controller.LoginController);
