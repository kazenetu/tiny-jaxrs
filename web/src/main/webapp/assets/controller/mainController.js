function MainController($location, webApiService, userService,storageService) {
    extendController(this, front.common.controller.PageBase);
    this.setTitle('メニュー');

    var ctrl = this;

    ctrl.gotoPage = function(pageUrl) {
        $location.path(pageUrl);
    }

}

angular.module('App').controller('mainController', MainController);