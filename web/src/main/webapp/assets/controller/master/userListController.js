front.controller.UserListController =  function UserListController($location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.SearchBase);
    this.setTitle('ユーザー検索');

    var ctrl = this;
    ctrl.totalPage = 0;



    function clearCondition(){
        storageService.clearValue(storageService.keys.condition);
    }

    function setConditions(pageIndex){
        var valus =
        {
            searchUserId : ctrl.searchUserId,
            pageIndex:pageIndex
        };
        storageService.setValue(storageService.keys.condition,valus);
    }

    function getConditions(){
        var values = storageService.getValue(storageService.keys.condition);

        if('pageIndex' in values){
            // 検索条件
            ctrl.searchUserId = values.searchUserId;

            // 検索(ページ指定)
            ctrl.search(values.pageIndex);
        }
    }

    ctrl.init = function() {
        getConditions();
    }

    ctrl.back = function(){
        $location.path('/main');
    }

    ctrl.create = function(){
        var values={
                userId : null
        };
        storageService.setValue(storageService.keys.updateKeys,values);
        $location.path('/useredit');
    }

    ctrl.search = function(pageIndex) {
        // 検索条件クリア
        clearCondition();

        webApiService.post('api/user/totalpage', {
            loginUserId: userService.getId(),
            requestData:{
                pageIndex : pageIndex,
                searchUserId : ctrl.searchUserId
            }
        }, function(response) {
            ctrl.totalPage = response.pageCount;

            ctrl.hideError();
            if(ctrl.totalPage < 0){
                ctrl.showError('検索結果が0件です');
                ctrl.totalPage = 0;
            }

            ctrl.paging(pageIndex,null);

            var values = ['/userlist','/useredit'];
            storageService.setValue(storageService.keys.enableConditionPaths,values);
        });
    }

    ctrl.edit = function(id){
        var values={
                userId : id
        };
        storageService.setValue(storageService.keys.updateKeys,values);
        $location.path('/useredit');
    }

    ctrl.getPage = function(pageIndex) {
        webApiService.postQuery('api/user/page', {
            loginUserId: userService.getId(),
            requestData:{
                pageIndex : pageIndex,
                searchUserId : ctrl.searchUserId
            }
        }, function(response) {
            ctrl.searchResult = response;

            setConditions(pageIndex)
        });
    }


    ctrl.userId = userService.getId();
    ctrl.userName = userService.getName();
}

angular.module('App').controller('userListController', front.controller.UserListController);