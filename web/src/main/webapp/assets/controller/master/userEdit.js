function UserEdit($location, webApiService, userService,storageService) {
    var ctrl = this;

    ctrl.isError = false;
    ctrl.errorMsg = "";

    ctrl.userId = "";
    ctrl.userName = "";
    ctrl.password = "";

    ctrl.init = function(){
        // 検索画面から取得したキー情報を設定
        var values= storageService.getValue(storageService.keys.updateKeys);

        ctrl.userId = values.userId;

        // ユーザーデータ取得
        webApiService.query('api/user/page?userId=:id&page=:pageIndex&searchUserId=:searchUserId', {
            id : userService.getId(),
            pageIndex : 0,
            searchUserId : ctrl.userId
        }, function(response) {
            ctrl.userName = response[0].name;
            ctrl.password = response[0].password;
        });
    }

    ctrl.update = function(){
        // ユーザーデータ更新
        webApiService.post('api/user/update', {
            loginUserId: userService.getId(),
            requestData:{
                id :ctrl.userId,
                name :ctrl.userName,
                password :ctrl.password
            }
        }, function(response) {
            if (response.result !== "OK") {
                ctrl.errorMsg = "更新失敗しました。";
                ctrl.isError = true;
            } else {
                ctrl.isError = false;
                $location.path('/main');
                storageService.clearValue(storageService.keys.updateKeys);
            }
        });

    }

    ctrl.cancel = function(){
        console.log("ctrl.userId:"+ctrl.userId);
        console.log("ctrl.userName:"+ctrl.userName);
        console.log("ctrl.password:"+ctrl.password);

        $location.path('/main');
        storageService.clearValue(storageService.keys.updateKeys);
    }

}

angular.module('App').controller('userEdit', UserEdit);
