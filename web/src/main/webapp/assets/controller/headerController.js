function HeaderController($location, userService, storageService) {
    var ctrl = this;

    ctrl.userName = function() {
        return userService.getName();
    };

    ctrl.passwordChange = function() {
        $location.path('/passwordChange');
    };

    ctrl.init = function(){
        var paths = storageService.getValue(storageService.keys.enableConditionPaths);
        var currentPath = $location.path();
        if(paths.indexOf(currentPath) < 0){
            storageService.clearValue(storageService.keys.condition);
        }
    };
}

angular.module('App').controller('headerController', HeaderController);
