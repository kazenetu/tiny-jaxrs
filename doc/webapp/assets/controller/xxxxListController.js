// TODO Xxxxをコントロール名に置き換える（頭文字を大文字） ※最終行も編集すること
front.controller.XxxxListController =  function XxxxListController($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.SearchBase);
    this.setTitle('ユーザー検索');

    var ctrl = this;
    // TODO プロパティを追加（ctrl.プロパティ名）
    ctrl.totalPage = 0; //例

    /**
     * ページ設定
     */
    var settings = {
        // TODO 総ページ数取得APIを設定する
        totalPageApiUrl : 'api/user/totalpage',
        // TODO 対象ページのレコード取得APIを設定する
        getPageApiUrl : 'api/user/page',
        // TODO 現在のページを設定する
        thisPage : '/master/userlist',
        // TODO 編集ページを設定する
        editPage : '/master/useredit',
        getSearchParam : function() {
            // TODO 検索条件を設定する
            return {
                searchUserId : ctrl.searchUserId
            };
        },
        setSearchControls : function(values) {
            // TODO 検索条件を復元する
            ctrl.searchUserId = values.searchUserId; //例
        }
    };

    /**
     * 戻るボタンクリック
     */
    ctrl.back = function(){
        // TODO 戻り先を設定
        $location.path('/main');
    }

    /**
     * 新規作成ボタンクリック
     */
    ctrl.create = function(){
        var values={
            // TODO 主キー名を設定する(値はnull)
            userId : null
        };
        storageService.setValue(storageService.keys.updateKeys,values);

        // 編集画面に遷移
        $location.path(settings.editPage);
    }

    /**
     * 一覧編集ボタンクリック
     */
    ctrl.edit = function(result){
        // 主キーの連想配列を変更キーStorageに設定
        var values={
            // TODO 編集画面に渡す値を設定する
            userId : result.id
        };
        storageService.setValue(storageService.keys.updateKeys,values);

        // 編集画面に遷移
        $location.path(settings.editPage);
    }

    /**
     * リクエストデータ取得
     */
    function getRequestData(pageIndex){
        var requestData =settings.getSearchParam();
        requestData['pageIndex'] = pageIndex;

        // ソート処理
        requestData['sortKey'] = ctrl.sortKey;
        requestData['sortType'] = ctrl.sortType;

        return requestData;
    }

    /**
     * 検索条件Storageの削除
     */
    function clearCondition(){
        storageService.clearValue(storageService.keys.condition);
    }

    /**
     * 検索条件Storageの設定
     */
    function setConditions(pageIndex){
        var values = getRequestData(pageIndex);
        storageService.setValue(storageService.keys.condition,values);
    }

    /**
     * 検索条件Storageから検索結果を再実行
     */
    function getConditions(){
        var values = storageService.getValue(storageService.keys.condition);

        // 検索条件Storageが設定されていれば検索を行う
        if('pageIndex' in values){
            // 検索条件
            settings.setSearchControls(values);

            // ソート処理
            ctrl.sortKey = values.sortKey;
            ctrl.sortType = values.sortType;

            // 検索(ページ指定)
            ctrl.search(values.pageIndex);
        }
    }

    /**
     * ページ初期化処理
     */
    ctrl.init = function() {
        // 検索条件Storageから検索結果を再実行
        getConditions();
    }

    /**
     * 検索処理
     * ボタンクリック時はpageIndexは0固定
     */
    ctrl.search = function(pageIndex) {
        // 検索条件クリア
        clearCondition();

        // 総ページ数の取得
        webApiService.post(settings.totalPageApiUrl, {
            loginUserId : userService.getId(),
            requestData : getRequestData(pageIndex)
        }, function(response) {
            ctrl.totalPage = response.responseData;

            ctrl.hideError();
            if(ctrl.totalPage < 0){
                ctrl.showError(response.errorMessage);
                ctrl.totalPage = 0;
            }

            // 対象ページのレコードを取得
            ctrl.paging(pageIndex,null);

            // 検索条件保存画面の指定(それ以外はヘッダーコントロールで削除)
            var values = [settings.thisPage, settings.editPage];
            storageService.setValue(storageService.keys.enableConditionPaths,values);
        });
    }

    /**
     * 検索ページ変更処理
     */
    ctrl.getPage = function(pageIndex) {
        // 対象ページのレコードを取得
        webApiService.post(settings.getPageApiUrl, {
            loginUserId : userService.getId(),
            requestData : getRequestData(pageIndex)
        }, function(response) {
            // 検索結果のレコードを設定
            ctrl.searchResult = response.responseData;

            // 検索条件Storageの設定
            setConditions(pageIndex)
        });
    }

    // ダウンロード処理用のID、名前を設定
    ctrl.userId = userService.getId();
    ctrl.userName = userService.getName();
}

// TODO Xxxxをコントロール名に置き換える（頭文字を大文字）
// TODO xxxxをコントロール名に置き換える（すべて小文字）
angular.module('App').controller('xxxxListController', front.controller.XxxxListController);
