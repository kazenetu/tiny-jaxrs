function UserEdit($location, webApiService, userService,storageService) {
    var ctrl = this;

    ctrl.isError = false;
    ctrl.errorMsg = "";

    ctrl.userId = "";
    ctrl.userName = "";
    ctrl.password = "";

    ctrl.init = function(){
        // 検索画面から取得したキー情報を設定
        ctrl.userId = storageService.getValue(storageService.KEY_EDIT_DATA);

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

    }

    ctrl.cancel = function(){
        console.log("ctrl.userId:"+ctrl.userId);
        console.log("ctrl.userName:"+ctrl.userName);
        console.log("ctrl.password:"+ctrl.password);

        $location.path('/main');
    }

}

angular.module('App').controller('userEdit', UserEdit);
