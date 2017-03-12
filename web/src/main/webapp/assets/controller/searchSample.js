function SearchSample($location, webApiService, userService,storageService) {

    var ctrl = this;
    ctrl.totalPage = 0;


    function clearCondition(){
        storageService.setValue(storageService.KEY_CONDITION,'');
    }

    function setConditions(pageIndex){
        var valus =
        {
            searchUserId : ctrl.searchUserId,
            pageIndex:pageIndex
        };
        storageService.setValue(storageService.KEY_CONDITION,JSON.stringify(valus));
    }

    function getConditions(){
        var valueString = storageService.getValue(storageService.KEY_CONDITION);
        if(valueString !== ""){
            values = JSON.parse(valueString);

            // 検索条件
            ctrl.searchUserId = values.searchUserId;

            // 検索(ページ指定)
            ctrl.search(values.pageIndex);
        }
    }

    ctrl.init = function() {
        getConditions();
    }

    ctrl.search = function(pageIndex) {
        // 検索条件クリア
        clearCondition();

        webApiService.get('api/user/totalpage?userId=:id&searchUserId=:searchUserId', {
            id : userService.getId(),
            searchUserId : ctrl.searchUserId
        }, function(response) {
            ctrl.totalPage = response.pageCount;
            ctrl.paging(pageIndex,null);
        });
    }

    ctrl.edit = function(id){
        console.log(id);
    }

    ctrl.getPage = function(pageIndex) {
        webApiService.query('api/user/page?userId=:id&page=:pageIndex&searchUserId=:searchUserId', {
            id : userService.getId(),
            pageIndex : pageIndex,
            searchUserId : ctrl.searchUserId
        }, function(response) {
            ctrl.searchResult = response;

            setConditions(pageIndex)
        });
    }

    ctrl.childlen = [];
    ctrl.sendRoot = function(src) {
        if (ctrl.childlen.indexOf(src) == -1) {
            ctrl.childlen.push(src);
        }
    }

    ctrl.paging = function(pageIndex, sender) {
        ctrl.getPage(pageIndex);

        ctrl.sendPageIndex(pageIndex, sender);
    }

    ctrl.sendPageIndex = function(pageIndex, sender) {
        var index = 0;
        while (index < ctrl.childlen.length) {
            if (ctrl.childlen[index] !== sender) {
                ctrl.childlen[index].setPagetIndex(pageIndex);
            }
            index++;
        }
    }

    ctrl.userId = userService.getId();
    ctrl.userName = userService.getName();
}

angular.module('App').controller('searchSample', SearchSample);
