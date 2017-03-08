var myApp = angular.module('userService', []);
myApp.service('userService', function() {
  this.userId = "";
  this.name = "";

  this.setId = function(id){
    this.id = id;
    sessionStorage.setItem("userId",this.id);
  };

  this.getId = function(){
    if(isValueNone(this.id)){
      this.id = sessionStorage.getItem("userId");
    }
    return this.id;
  };

  this.setName = function(name){
    this.name = name;
    sessionStorage.setItem("userName",this.name);
  };

  this.getName = function(){
    if(isValueNone(this.name)){
      this.name = sessionStorage.getItem("userName");
    }
    return this.name;
  };

  function isValueNone(value){
	  return value===null || value === undefined || value === "";
  }
});
