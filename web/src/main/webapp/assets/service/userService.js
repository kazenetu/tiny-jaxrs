var myApp = angular.module('userService', []);
myApp.service('userService', function() {
  this.userId = "";
  this.name = "";

  this.setId = function(id){
    this.id = id;
    sessionStorage.setItem("userId",this.id);
  };

  this.getId = function(){
    if(this.id === ""){
      this.id = sessionStorage.getItem("userId");
    }
    return this.id;
  };

  this.setName = function(name){
    this.name = name;
    sessionStorage.setItem("userName",this.name);
  };

  this.getName = function(){
    if(this.name === ""){
      this.name = sessionStorage.getItem("userName");
    }
    if(this.name === null || this.name===""){
      return "ななし";
    }
    return this.name;
  };
});
