front.controller.UserEdit = function UserEdit($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('ユーザー編集');

    var ctrl = this;

    ctrl.disabledUserId = true;
    ctrl.userId = "";
    ctrl.userName = "";
    ctrl.password = "";

    ctrl.userIdIcon = '';

    ctrl.init = function(){
        // 検索画面から取得したキー情報を設定
        var values= storageService.getValue(storageService.keys.updateKeys);

        if(values.userId === null){
            ctrl.disabledUserId = false;

        }else{
            ctrl.disabledUserId = true;
            ctrl.userId = values.userId;

            // ユーザーデータ取得
            webApiService.postQuery('api/user/page', {
                loginUserId: userService.getId(),
                requestData:{
                    pageIndex : 0,
                    searchUserId : ctrl.userId
                }
            }, function(response) {
                ctrl.userName = response[0].name;
                ctrl.password = response[0].password;
            });
        }
    }

    ctrl.duplicateUserId = function(){
        ctrl.userIdIcon = '';

        if(!ctrl.disabledUserId){
            // ユーザーデータ取得
            webApiService.postQuery('api/user/page', {
                loginUserId: userService.getId(),
                requestData:{
                    pageIndex : 0,
                    searchUserId : ctrl.userId
                }
            }, function(response) {
                if(response.length <= 0){
                    ctrl.userIdIcon = 'glyphicon-ok';
                }else{
                    ctrl.userIdIcon = 'glyphicon-remove';
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
        console.log("ctrl.userId:"+ctrl.userId);
        console.log("ctrl.userName:"+ctrl.userName);
        console.log("ctrl.password:"+ctrl.password);

        $location.path('/userlist');
        storageService.clearValue(storageService.keys.updateKeys);
    }
}

angular.module('App').controller('userEdit', front.controller.UserEdit);
