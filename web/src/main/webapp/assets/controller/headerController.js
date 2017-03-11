function HeaderController($location, userService) {
    var ctrl = this;

    ctrl.userName = function() {
        return userService.getName();
    };

    ctrl.passwordChange = function() {
        $location.path('/passwordChange');
    };
}

angular.module('App').controller('headerController', HeaderController);
