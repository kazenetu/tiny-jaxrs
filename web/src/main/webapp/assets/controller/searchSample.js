function SearchSample($location, webApiService, userService) {

    var ctrl = this;
    ctrl.totalPage = 0;

    ctrl.search = function() {
        webApiService.get('api/user/totalpage?userId=:id&searchUserId=:searchUserId', {
            id : userService.getId(),
            searchUserId : ctrl.searchUserId
        }, function(response) {
            ctrl.totalPage = response.pageCount;
            ctrl.sendPageIndex(0, null);

            ctrl.getPage(0);
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
