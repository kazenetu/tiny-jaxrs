function MainController($location, webApiService, userService,storageService) {

    var ctrl = this;

    ctrl.gotoPage = function(pageUrl) {
        $location.path(pageUrl);
    }

    ctrl.header = null;
    ctrl.sendRootHeader = function(src){
        ctrl.header = src;
    }
}

angular.module('App').controller('mainController', MainController);