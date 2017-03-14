function PasswordChangeController($location, webApiService, userService) {
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

    ctrl.header = null;
    ctrl.sendRootHeader = function(src){
        ctrl.header = src;
    }
}

angular.module('App').controller('passwordChangeController', PasswordChangeController);
