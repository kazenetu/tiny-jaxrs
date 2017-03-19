function PasswordChangeController($location, webApiService, userService) {
    extendController(this, front.common.controller.PageBase);
    this.setTitle('パスワード変更');

    var ctrl = this;

    ctrl.passwordChange = function() {
        webApiService.post('api/user/passwordChange', {
            id : userService.getId(),
            password : ctrl.password,
            newPassword : ctrl.newPassword
        }, function(response) {
            if (response.result !== 'OK') {
                ctrl.header.showError('現在のパスワードが異なります');
            } else {
                ctrl.header.hideError();
                $location.path('/main');
            }
        });
    }

    ctrl.cancel = function(){
        $location.path('/main');
    }

}

angular.module('App').controller('passwordChangeController', PasswordChangeController);
