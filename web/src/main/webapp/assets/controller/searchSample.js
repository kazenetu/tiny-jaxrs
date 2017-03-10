var myApp = angular.module('searchSample', ['webApiService', 'userService']);

myApp.controller('searchSample', ['$scope', '$location', 'webApiService', 'userService',
    function ($scope, $location, webApiService, userService) {

    var ctrl = this;
	$scope.pageIndex = 0;
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
        		$scope.pageIndex = 0;

        		ctrl.getPage(0);
             });

    }
    $scope.prev = function () {
    	if($scope.pageIndex>0){
    		$scope.pageIndex--;
    		ctrl.getPage($scope.pageIndex);
    	}
    }
    $scope.next = function () {
    	if($scope.pageIndex<$scope.totalPage-1){
    		$scope.pageIndex++;
    		ctrl.getPage($scope.pageIndex);
    	}
    }

    ctrl.getPage = function(pageIndex){
        webApiService.query('api/user/page?userId=:id&page=:pageIndex&searchUserId=:searchUserId',
            { id: userService.getId(),pageIndex:pageIndex,searchUserId:$scope.searchUserId },
            function (response) {
                $scope.searchResult = response;
             });
    }

    $scope.test = function(index){
        ctrl.getPage(index);
    }

    $scope.userId = userService.getId();
    $scope.userName = userService.getName();
}
]);
