// TODO Xxxxをコントロール名に置き換える（頭文字を大文字）
// TODO xxxxをコントロール名に置き換える（すべて小文字）
front.controller.XxxxListController =  function XxxxListController($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.SearchBase);
    this.setTitle('画面名'); //TODO 画面名を入力する

    var ctrl = this;
    // TODO プロパティを追加（ctrl.プロパティ名）
    ctrl.totalPage = 0; //例

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
      // TODO 検索条件を設定する
      var values =
      {
          searchUserId : ctrl.searchUserId //例
      };
      values['pageIndex'] = pageIndex;
      storageService.setValue(storageService.keys.condition,values);
    }

    /**
     * 検索条件Storageから検索結果を再実行
     */
    function getConditions(){
        var values = storageService.getValue(storageService.keys.condition);

        // 検索条件Storageが設定されていれば検索を行う
        if('pageIndex' in values){
            // TODO 検索条件を設定する
            ctrl.searchUserId = values.searchUserId; //例

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
        // TODO 戻り先を設定
        $location.path('/戻り先');
    }

    /**
     * 新規作成ボタンクリック
     */
    ctrl.create = function(){
        // TODO 編集画面に渡す値を設定する
        var values={
                userId : null
        };

        storageService.setValue(storageService.keys.updateKeys,values);

        // TODO 編集画面を設定する
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
        // TODO 総ページ数取得APIを設定する
        webApiService.post('api/user/totalpage', {
            loginUserId: userService.getId(),
            requestData:{
                pageIndex : pageIndex,
                // TODO 検索条件を設定する
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
            // TODO 検索画面と編集画面を設定する
            var values = ['/master/userlist','/master/useredit'];

            storageService.setValue(storageService.keys.enableConditionPaths,values);
        });
    }

    /**
     * 一覧編集ボタンクリック
     */
    ctrl.edit = function(id){
        // TODO 編集画面に渡す値を設定する
        var values={
                userId : id
        };

        storageService.setValue(storageService.keys.updateKeys,values);

        // TODO 編集画面を設定する
        $location.path('/master/useredit');
    }

    /**
     * 検索ページ変更処理
     */
    ctrl.getPage = function(pageIndex) {
        // 対象ページのレコードを取得
        // TODO 対象ページのレコード取得APIを設定する
        webApiService.post('api/user/page', {
            loginUserId: userService.getId(),
            requestData:{
                pageIndex : pageIndex,
                // TODO 検索条件を設定する
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

angular.module('App').controller('xxxxListController', front.controller.XxxxListController);
