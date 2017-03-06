var myApp = angular.module('passwordChangeController', ['webApiService','userService']);

myApp.controller('passwordChangeController', ['$scope','$location','webApiService','userService',
    function($scope, $location, webApiService, userService){
      $scope.isError = false;
      $scope.errorMsg = "";

      $scope.passwordChange = function(){
          webApiService.post('api/user/passwordChange',
              {id:userService.getId(),password:$scope.password,newPassword:$scope.newPassword},
              function(response){
                  if(response.result !== "OK"){
                    $scope.errorMsg = "現在のパスワードが異なります";
                    $scope.isError = true;
                  }else{
                      $location.path('/main');
                  }
               }
          );
      }
    }
]);
