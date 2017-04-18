front.controller.NumbercheckController =  function NumbercheckController($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('数値入力チェック');

    var ctrl = this;
    ctrl.inputTest = '';
    ctrl.inputAlpha = '';
    ctrl.inputKana = '';

    /**
     * 入力チェック
     */
    ctrl.inputCheck = function() {

        ctrl.hideError();
    }

}

angular.module('App').controller('numbercheckController', front.controller.NumbercheckController);
