front.controller.UserEdit = function UserEdit($q, $location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('ユーザー編集');

    var ctrl = this;

    // 入力情報
    ctrl.userId = "";
    ctrl.userName = "";
    ctrl.password = "";
    ctrl.birthDay = null;
    ctrl.time = null;
    ctrl.ts = null;

    // 入力情報のエラークラス
    ctrl.errorUserId = "";
    ctrl.errorUserName = "";
    ctrl.errorPassword = "";
    ctrl.errorBirthDay = "";
    ctrl.errorTime = "";
    ctrl.errorTs = "";

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
     * ページ設定
     */
    var settings = {
        findApiUrl : 'api/user/find',
        insertApiUrl : 'api/user/insert',
        updateApiUrl : 'api/user/update',
        deleteApiUrl : 'api/user/delete',
        getRequestData : function() {
            return {
                id : ctrl.userId
            };
        },
        setEditMode : function(values) {
        settings.createMode = (values.userId === null);
        },
        setEditControls : function(values) {
                ctrl.userName = values.name;
                ctrl.password = values.password;
                if(values.date !== null){
                    ctrl.birthDay = new Date(values.date);
                }
                if(values.time !== null){
                    ctrl.time = new Date("1970/01/01 " + values.time);
                }
                if(values.ts !== null){
                    ctrl.ts = new Date(values.ts);
                }
        },
        listPage : '/master/userlist',

        createMode : false,
        isCreateMode : function() {
            return settings.createMode;
        }
    };

    /**
     * 初期化イベント
     */
    ctrl.init = function(){
        // 検索画面から取得したキー情報を設定
        var values= storageService.getValue(storageService.keys.updateKeys);

        // 新規作成モードか否かの設定
        settings.setEditMode(values);

        // 新規作成モードか否かによって表示内容を変更
        if(settings.isCreateMode()){
            // 新規作成
            ctrl.commmitButtonName = '登録';

        }else{
            // 更新
            ctrl.commmitButtonName = '更新';
            ctrl.userId = values.userId;

            // データ取得
            webApiService.post(settings.findApiUrl, {
                loginUserId : userService.getId(),
                requestData : settings.getRequestData()
            }, function(response) {
                // 取得結果をコントロールに設定
                settings.setEditControls(response.responseData);
            });
        }
    }

    /**
     * キーカラム重複チェック
     */
    ctrl.duplicateUserId = function(){
        // ユーザーIDの重複アイコンは「なし」
        ctrl.userIdIcon = ctrl.ICONS.NONE;

        // 新規作成時のみチェックする
        if(settings.isCreateMode()){
            if(ctrl.userId === ''){
                ctrl.userIdIcon = ctrl.ICONS.NG;
                return;
            }

            // ユーザーデータ取得
            webApiService.post(settings.findApiUrl, {
                loginUserId : userService.getId(),
                requestData : settings.getRequestData()
            }, function(response) {
                if(response.responseData === null){
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

        var d = $q.defer();
        d.promise
        .then(ctrl.showConfirm($q,ctrl.commmitButtonName + 'の確認',
                'このユーザーを' + ctrl.commmitButtonName +'しますか', ctrl.commmitButtonName +'する'))
        .then(function(){
            var deferrred = $q.defer();

            var apiUrl = settings.updateApiUrl;
            if(settings.isCreateMode()){
                apiUrl = settings.insertApiUrl;
            }

            // ユーザーデータ更新
            webApiService.post(apiUrl, {
                loginUserId: userService.getId(),
                requestData:{
                    id :ctrl.userId,
                    name :ctrl.userName,
                    date :ctrl.birthDay,
                    time :ctrl.time.toLocaleTimeString(),
                    ts :ctrl.ts,
                    password :ctrl.password
                }
            }, function(response) {
                if (response.result !== 'OK') {
                    ctrl.showError(response.errorMessage);
                } else {
                    ctrl.hideError();

                    deferrred.resolve();
                }
            });

            return deferrred.promise;
        })
        .then(ctrl.showMsgDialog($q,ctrl.commmitButtonName + 'の報告',
                'このユーザーを' + ctrl.commmitButtonName +'しました', '確認'))
        .then(function(){
            $location.path(settings.listPage);
            storageService.clearValue(storageService.keys.updateKeys);
        });
        // 発火
        d.resolve();
    }

    /**
     * 削除イベント
     */
    ctrl.delete = function(){
        var d = $q.defer();
        d.promise
        .then(ctrl.showConfirm($q,'削除の確認','このユーザーを削除しますか','削除する'))
        .then(function(){
            var deferrred = $q.defer();

            // データ削除
            webApiService.post(settings.deleteApiUrl, {
                loginUserId: userService.getId(),
                requestData:{
                    id :ctrl.userId,
                    name :ctrl.userName,
                    password :ctrl.password
                }
            }, function(response) {
                if (response.result !== 'OK') {
                    ctrl.showError(response.errorMessage);
                } else {
                    ctrl.hideError();

                    deferrred.resolve();
                }
            });

            return deferrred.promise;
        })
        .then(ctrl.showMsgDialog($q,'削除の報告','このユーザーを削除しました', '確認'))
        .then(function(){
            $location.path(settings.listPage);
            storageService.clearValue(storageService.keys.updateKeys);
        });
        // 発火
        d.resolve();
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
            if(ctrl.userId === ''){
                ctrl.showError('ユーザーIDを入力してください');
                ctrl.errorUserId = 'has-error';
                return false;
            }
            // ユーザーID重複アイコンがNGの場合はエラー
            if(ctrl.userIdIcon === ctrl.ICONS.NG){
                ctrl.showError('ユーザーIDはすでに登録されています');
                ctrl.errorUserId = 'has-error';
                return false;
            }
        }

        if(ctrl.userName === ''){
            ctrl.showError('ユーザー名を入力してください');
            ctrl.errorUserName = 'has-error';
            return false;
        }

        if(ctrl.birthDay === null){
            ctrl.showError('誕生日を入力してください');
            ctrl.errorBirthDay = 'has-error';
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
        $location.path(settings.listPage);
        storageService.clearValue(storageService.keys.updateKeys);
    }

    /**
     * 新規モードか否か
     */
    ctrl.isCreateMode = function() {
        return settings.isCreateMode();
    }
}

angular.module('App').controller('userEdit', front.controller.UserEdit);
