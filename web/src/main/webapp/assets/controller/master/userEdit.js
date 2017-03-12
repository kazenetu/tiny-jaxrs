function UserEdit($location, webApiService, userService,storageService) {
    var ctrl = this;

    ctrl.isError = false;
    ctrl.errorMsg = "";

    ctrl.userId = "";
    ctrl.userName = "";
    ctrl.password = "";

    ctrl.init = function(){
        ctrl.userId = storageService.getValue(storageService.KEY_EDIT_DATA);
    }

    ctrl.update = function(){

    }

    ctrl.cancel = function(){
        $location.path('/main');
    }

}

angular.module('App').controller('userEdit', UserEdit);
