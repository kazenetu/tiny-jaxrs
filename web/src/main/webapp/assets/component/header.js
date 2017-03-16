/**
 * ヘッダーコンポーネント
 */
function HeaderController($location, userService, webApiService, storageService) {
    var ctrl = this;

    ctrl.isError = false;
    ctrl.errorMsg = '';

    ctrl.userName = function() {
        return userService.getName();
    };

    ctrl.passwordChange = function() {
        $location.path('/passwordChange');
    };

    /**
     * ロード完了イベント
     */
    ctrl.$onInit = function() {
        // 呼び出し元にインスタンスを登録
        ctrl.onSendRoot({src:ctrl});

        // 検索結果表示許可画面以外の場合は検索条件をクリア
        var paths = storageService.getValue(storageService.keys.enableConditionPaths);
        var currentPath = $location.path();
        if(Array.isArray(paths) && paths.indexOf(currentPath) < 0){
            storageService.clearValue(storageService.keys.condition);
        }
    };

    /**
     * ログアウト
     */
    ctrl.logout = function() {
        // ユーザーデータ取得
        webApiService.post('api/user/logout', {
        }, function(response) {
            $location.path('/');
        });
    };

    /**
     * エラーメッセージ表示
     */
    ctrl.showError = function(message) {
        ctrl.isError = true;
        ctrl.errorMsg = message;
    }

    /**
     * エラーメッセージ非表示
     */
    ctrl.hideError = function() {
        ctrl.isError = false;
        ctrl.errorMsg = '';
    }

}

/**
 * コンポーネントを登録
 */
myApp.component("header", {
    bindings : {
        onSendRoot:'&'
    },
    templateUrl:'view/header.html',
    controller : HeaderController
});
