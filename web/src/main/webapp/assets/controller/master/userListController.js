front.controller.UserListController =  function UserListController($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.SearchBase);
    this.setTitle('ユーザー検索');

    var ctrl = this;
    ctrl.totalPage = 0;


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
        var valus =
        {
            searchUserId : ctrl.searchUserId,
            pageIndex:pageIndex
        };
        storageService.setValue(storageService.keys.condition,valus);
    }

    /**
     * 検索条件Storageから検索結果を再実行
     */
    function getConditions(){
        var values = storageService.getValue(storageService.keys.condition);

        // 検索条件Storageが設定されていれば検索を行う
        if('pageIndex' in values){
            // 検索条件
            ctrl.searchUserId = values.searchUserId;

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
        $location.path('/master/useredit');
    }

    /**
     * 検索処理
     * ボタンクリック時はpageIndexは0固定
     */
    ctrl.search = function(pageIndex) {
        // 検索条件クリア
        clearCondition();

        // 総ページ数の取得
        webApiService.post('api/user/totalpage', {
            loginUserId: userService.getId(),
            requestData:{
                pageIndex : pageIndex,
                searchUserId : ctrl.searchUserId
            }
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
            var values = ['/master/userlist','/master/useredit'];
            storageService.setValue(storageService.keys.enableConditionPaths,values);
        });
    }

    /**
     * 一覧編集ボタンクリック
     */
    ctrl.edit = function(id){
        // 主キーの連想配列を変更キーStorageに設定
        var values={
                userId : id
        };
        storageService.setValue(storageService.keys.updateKeys,values);

        // 編集画面に遷移
        $location.path('/master/useredit');
    }

    /**
     * 検索ページ変更処理
     */
    ctrl.getPage = function(pageIndex) {
        // 対象ページのレコードを取得
        webApiService.post('api/user/page', {
            loginUserId: userService.getId(),
            requestData:{
                pageIndex : pageIndex,
                searchUserId : ctrl.searchUserId
            }
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

angular.module('App').controller('userListController', front.controller.UserListController);
