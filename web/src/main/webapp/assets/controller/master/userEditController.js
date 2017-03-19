front.controller.UserEdit = function UserEdit($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('ユーザー編集');

    var ctrl = this;

    ctrl.disabledUserId = true;
    ctrl.userId = "";
    ctrl.userName = "";
    ctrl.password = "";

    ctrl.userIdIcon = '';
    ctrl.ICONS = {
        NONE:'none',
        OK: 'glyphicon-ok',
        NG: 'glyphicon-remove',
    };
    ctrl.commmitButtonName = '';

    ctrl.init = function(){
        // 検索画面から取得したキー情報を設定
        var values= storageService.getValue(storageService.keys.updateKeys);

        if(values.userId === null){
            ctrl.disabledUserId = false;
            ctrl.commmitButtonName = '登録';

        }else{
            ctrl.disabledUserId = true;
            ctrl.commmitButtonName = '更新';
            ctrl.userId = values.userId;

            // ユーザーデータ取得
            webApiService.postQuery('api/user/find', {
                loginUserId: userService.getId(),
                requestData:{
                    id : ctrl.userId
                }
            }, function(response) {
                ctrl.userName = response[0].name;
                ctrl.password = response[0].password;
            });
        }
    }

    ctrl.duplicateUserId = function(){
        ctrl.userIdIcon = ctrl.ICONS.NONE;

        if(!ctrl.disabledUserId){
            if(ctrl.userId === ''){
                ctrl.userIdIcon = ctrl.ICONS.NG;
                return;
            }

            // ユーザーデータ取得
            webApiService.postQuery('api/user/find', {
                loginUserId: userService.getId(),
                requestData:{
                    id : ctrl.userId
                }
            }, function(response) {
                if(response.length <= 0){
                    ctrl.userIdIcon = ctrl.ICONS.OK;
                }else{
                    ctrl.userIdIcon = ctrl.ICONS.NG;
                }
            });
        }
    }

    ctrl.insertOrUpdate = function(){
        var method = 'insert';
        if(ctrl.disabledUserId){
            method = 'update';
        }

        // ユーザーデータ更新
        webApiService.post('api/user/' + method, {
            loginUserId: userService.getId(),
            requestData:{
                id :ctrl.userId,
                name :ctrl.userName,
                password :ctrl.password
            }
        }, function(response) {
            if (response.result !== 'OK') {
                ctrl.header.showError('更新失敗しました。');
            } else {
                ctrl.header.hideError();
                $location.path('/userlist');
                storageService.clearValue(storageService.keys.updateKeys);
            }
        });

    }

    ctrl.cancel = function(){
        $location.path('/userlist');
        storageService.clearValue(storageService.keys.updateKeys);
    }
}

angular.module('App').controller('userEdit', front.controller.UserEdit);
