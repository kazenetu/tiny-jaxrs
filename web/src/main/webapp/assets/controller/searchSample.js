var myApp = angular.module('searchSample', ['webApiService', 'userService']);

myApp.controller('searchSample', ['$scope', '$location', 'webApiService', 'userService',
    function ($scope, $location, webApiService, userService) {

    var ctrl = this;
	$scope.totalPage =0;

	$scope.search = function () {
        /*
        webApiService.query('api/user/list?userId=:id', { id: userService.getId() },
            function (response) {
                $scope.searchResult = response;
             });
        */

        webApiService.get('api/user/totalpage?userId=:id&searchUserId=:searchUserId',
    		{ id: userService.getId(),searchUserId:$scope.searchUserId },
            function (response) {
        		$scope.totalPage = response.pageCount;
                ctrl.sendPageIndex(0,null);

                ctrl.getPage(0);
             });

    }


    ctrl.getPage = function(pageIndex){
        webApiService.query('api/user/page?userId=:id&page=:pageIndex&searchUserId=:searchUserId',
            { id: userService.getId(),pageIndex:pageIndex,searchUserId:$scope.searchUserId },
            function (response) {
                $scope.searchResult = response;
             });
    }

    ctrl.childlen = [];
    $scope.sendRoot = function(src){
        if(ctrl.childlen.indexOf(src) == -1){
            ctrl.childlen.push(src);
        }
    }

    $scope.paging = function(pageIndex,sender){
        ctrl.getPage(pageIndex);

        ctrl.sendPageIndex(pageIndex,sender);
    }

    ctrl.sendPageIndex = function(pageIndex,sender){
        var index=0;
        while(index < ctrl.childlen.length){
            if(ctrl.childlen[index] !== sender){
                ctrl.childlen[index].setPagetIndex(pageIndex);
            }
            index++;
        }
    }

    $scope.userId = userService.getId();
    $scope.userName = userService.getName();
}
]);
