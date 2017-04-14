front.controller.InputcheckController =  function InputcheckController($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('入力チェック');

    var ctrl = this;
    ctrl.inputTest = '';
    ctrl.inputAlpha = '';
    ctrl.inputKana = '';

    /**
     * 入力チェック
     */
    ctrl.inputCheck = function() {
        // 未入力
        if(ctrl.inputTest === ''){
            ctrl.showError('未入力エラー');
            return;
        }

        // 最大桁数超過
        if(ctrl.isOverMaxLength(ctrl.inputTest,10)){
            ctrl.showError('最大桁数オーバー');
            return;
        }

        // 英数字チェック
        if(!ctrl.isNumAlpha(ctrl.inputTest)){
            ctrl.showError('英数字エラー');
            return;
        }

        // 英字チェック
        if(ctrl.inputAlpha !== ''){
            if(!ctrl.isAlpha(ctrl.inputAlpha)){
                ctrl.showError('英字エラー');
                return;
            }
        }

        // カタカナチェック
        if(ctrl.inputKana !== ''){
            if(!ctrl.isKana(ctrl.inputKana)){
                ctrl.showError('カタカナエラー');
                return;
            }
        }

        ctrl.hideError();
    }

    ctrl.inputSingle = '';

    ctrl.singleByteChangeEvent = function(fieldName) {
        if(!this[fieldName]){
            return;
        }

        var debug = '['+ctrl.inputSingle;

        this[fieldName] = this[fieldName].replace(/[^\s\w]/g,'').replace(/　/g,'');

        debug += ']->[' + ctrl.inputSingle+']';
        console.log(debug);
    }
}

angular.module('App').controller('inputcheckController', front.controller.InputcheckController);
