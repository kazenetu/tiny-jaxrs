front.controller.NoPagerListController =  function NoPagerListController($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.SearchBase);
    this.setTitle('ページャーなし検索サンプル');

    var ctrl = this;
    ctrl.totalPage = 0;

    ctrl.getOrder = function(){
        var result=ctrl.sortKey;
        if(ctrl.sortType !== ''){
            if(ctrl.sortType === 'DESC') {
                result = '-' +result;
            }
        }
        return result;
    }

    ctrl.getFilter = function(){
        return {
            id : ctrl.searchUserId
        };
    }

    /**
     * ページ設定
     */
    var settings = {
        totalPageApiUrl : 'api/user/totalpage',
        getPageApiUrl : 'api/user/pages',
        thisPage : '/master/userlist',
        editPage : '/master/useredit',
        getSearchParam : function() {
            return {
                searchUserId : ctrl.searchUserId
            };
        },
        setSearchControls : function(values) {
            ctrl.searchUserId = values.searchUserId;
        }
    };

    /**
     * 戻るボタンクリック
     */
    ctrl.back = function(){
        $location.path('/main');
    }

    /**
     * 新規作成ボタンクリック
     */
    ctrl.create = function(){
        var values={
                userId : null
        };
        storageService.setValue(storageService.keys.updateKeys,values);
        $location.path(settings.editPage);
    }

    /**
     * 一覧編集ボタンクリック
     */
    ctrl.edit = function(result){
        // 主キーの連想配列を変更キーStorageに設定
        var values={
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
            ctrl.getPages(pageIndex);

            // 検索条件保存画面の指定(それ以外はヘッダーコントロールで削除)
            var values = [settings.thisPage, settings.editPage];
            storageService.setValue(storageService.keys.enableConditionPaths,values);
        });
    }

    /**
     * 検索ページ変更処理
     */
    ctrl.getPage = function(pageIndex) {
        setConditions(pageIndex)
    }

    /**
     * 検索ページ取得処理
     */
    ctrl.getPages = function(pageIndex) {
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

angular.module('App').controller('noPagerListController', front.controller.NoPagerListController);