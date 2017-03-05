var myApp = angular.module('webApiService', []);
myApp.service('webApiService', function($resource, $location) {

  this.baseUri = "";

  /**
   * 基本URIを設定する
   */
  this.setBaseUri = function(uri){
    this.baseUri = uri;
  }

  /**
   * GET(配列取得)メソッド
   */
  this.query = function(action,params,callFunction){
    var result = $resource(this.baseUri + action).query(params);

    result.$promise.then(function (response) {
    	callFunction(response);
    }, function (response) {
        $location.path('/');
    });
  };

  /**
   * GETメソッド
   */
  this.get = function(action,params,callFunction){
    var result = $resource(this.baseUri + action).get(params);

    result.$promise.then(function (response) {
    	callFunction(response);
    }, function (response) {
        $location.path('/');
    });
  };

  /**
   * POSTメソッド
   */
  this.post = function(action,params,callFunction){
    var result = $resource(this.baseUri + action).save(params);

    result.$promise.then(function (response) {
    	callFunction(response);
    }, function (response) {
        $location.path('/');
    });
  };

});
