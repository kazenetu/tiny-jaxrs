front.controller.NumbercheckController =  function NumbercheckController($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('数値入力チェック');

    var ctrl = this;
    ctrl.inputNumber = '';
    ctrl.intPartCount = 3;
    ctrl.decimalPartCount = 0;

    /**
     * 入力チェック
     */
    ctrl.inputCheck = function() {
        // 未入力
        if(ctrl.inputNumber === ''){
            ctrl.showError('未入力エラー');
            return;
        }

        // 数値入力
        if(!ctrl.isNumber(ctrl.inputNumber,
                ctrl.intPartCount, ctrl.decimalPartCount)){
            ctrl.showError('数値エラー');
            return;
        }

        ctrl.hideError();
    }

    /**
     * 数値チェック
     */
    ctrl.isNumber = function(src, intPartCount, decimalPartCount) {
        if(src === null || src === undefined) {
            return true;
        }

        if(decimalPartCount === null || decimalPartCount === undefined) {
            decimalPartCount = 0;
        }
        var regString = '';
        var count;
        // 整数部
        if(intPartCount>1){
            regString += '[1-9]?';
            intPartCount -= 1;
        }
        regString += '[0-9]{1,' + intPartCount + '}';

        // 小数部
        if(decimalPartCount > 0){
            regString += '[.]?[0-9]{0,' + decimalPartCount + '}';
            for(count=decimalPartCount;count>0;count--){
            }
        }

        return new RegExp('^'+ regString + '$').test(src);
    }


}

angular.module('App').controller('numbercheckController', front.controller.NumbercheckController);
