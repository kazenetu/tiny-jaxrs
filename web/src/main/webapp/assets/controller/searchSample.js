var myApp = angular.module('searchSample', ['webApiService', 'userService']);

myApp.controller('searchSample', ['$scope', '$location', 'webApiService', 'userService',
    function ($scope, $location, webApiService, userService) {

	$scope.pageIndex = 0;
	$scope.totalPage =0;

	$scope.search = function () {

        /*
        webApiService.query('api/user/list?userId=:id', { id: userService.getId() },
            function (response) {
                $scope.searchResult = response;
             });
        */

        webApiService.get('api/user/totalpage?userId=:id', { id: userService.getId() },
            function (response) {
        		$scope.totalPage = response.pageCount;
        		$scope.pageIndex = 0;

                getPage();
             });

    }
    $scope.prev = function () {
    	if($scope.pageIndex>0){
    		$scope.pageIndex--;
    		getPage();
    	}
    }
    $scope.next = function () {
    	if($scope.pageIndex<$scope.totalPage-1){
    		$scope.pageIndex++;
    		getPage();
    	}
    }


    var getPage = function(){
        webApiService.query('api/user/page?userId=:id&page=:pageIndex',
            { id: userService.getId(),pageIndex:$scope.pageIndex },
            function (response) {
                $scope.searchResult = response;
             });
    }

    $scope.userId = userService.getId();
    $scope.userName = userService.getName();
}
]);
