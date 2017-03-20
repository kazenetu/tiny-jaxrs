front.controller.UserEdit = function UserEdit($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('ユーザー編集');

    var ctrl = this;

    // 入力情報
    ctrl.userId = "";
    ctrl.userName = "";
    ctrl.password = "";

    // 入力情報のエラークラス
    ctrl.errorUserId = "";
    ctrl.errorUserName = "";
    ctrl.errorPassword = "";


    /**
     * ユーザーIDの編集可否
     * true：編集不可（編集モード）
     * false：編集可（新規作成モード）
     */
    ctrl.disabledUserId = true;

    /**
     * ユーザーID重複チェック
     */
    ctrl.userIdIcon = '';

    /**
     * ユーザーID重複チェック用列挙体
     */
    ctrl.ICONS = {
        NONE:'none',
        OK: 'glyphicon-ok',
        NG: 'glyphicon-remove',
    };

    /**
     * DB反映ボタンの表示名
     */
    ctrl.commmitButtonName = '';

    /**
     * 初期化イベント
     */
    ctrl.init = function(){
        // 検索画面から取得したキー情報を設定
        var values= storageService.getValue(storageService.keys.updateKeys);

        if(values.userId === null){
            // 新規作成
            ctrl.disabledUserId = false;
            ctrl.commmitButtonName = '登録';

        }else{
            // 更新
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

    /**
     * ユーザーID重複チェック
     */
    ctrl.duplicateUserId = function(){
        // ユーザーIDの重複アイコンは「なし」
        ctrl.userIdIcon = ctrl.ICONS.NONE;

        // 新規作成時のみチェックする
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
                    // レコードがなければOKアイコン
                    ctrl.userIdIcon = ctrl.ICONS.OK;
                    ctrl.checkClearRequired('errorUserId');
                }else{
                    //レコードがあればNGアイコン
                    ctrl.userIdIcon = ctrl.ICONS.NG;
                }
            });
        }
    }

    /**
     * 必須入力エラー後に入力があるか
     */
    ctrl.checkClearRequired = function(target){
        if(this[target] !== ''){
            this[target] = '';
            ctrl.hideError();
        }
    }

    /**
     * 登録または更新イベント
     */
    ctrl.insertOrUpdate = function(){
        // 入力チェック
        if(!validateCheck()){
            return;
        }

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
                ctrl.showError(ctrl.commmitButtonName + '失敗しました。');
            } else {
                ctrl.hideError();
                $location.path('/userlist');
                storageService.clearValue(storageService.keys.updateKeys);
            }
        });
    }

    /**
     * DB反映前の入力チェック
     */
    function validateCheck(){
        // エラーなし状態に設定
        ctrl.hideError();
        ctrl.errorUserId = '';
        ctrl.errorUserName = '';
        ctrl.errorPassword = '';

        // 新規作成モードのみのチェック
        if(!ctrl.disabledUserId){
            // 重複チェックを実行
            ctrl.duplicateUserId();
            // ユーザーID重複アイコンがNGの場合はエラー
            if(ctrl.userIdIcon === ctrl.ICONS.NG){
                ctrl.showError('ユーザーIDを確認してください');
                ctrl.errorUserId = 'has-error';
                return false;
            }
        }

        if(ctrl.userName === ''){
            ctrl.showError('ユーザー名を入力してください');
            ctrl.errorUserName = 'has-error';
            return false;
        }

        if(ctrl.password === ''){
            ctrl.showError('パスワードを入力してください');
            ctrl.errorPassword = 'has-error';
            return false;
        }

        return true;
    }

    /**
     * 戻るイベント
     */
    ctrl.cancel = function(){
        $location.path('/userlist');
        storageService.clearValue(storageService.keys.updateKeys);
    }
}

angular.module('App').controller('userEdit', front.controller.UserEdit);
