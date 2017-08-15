front.controller.FileUploadController =  function FileUploadController($location, webApiService, userService,storageService,$http) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('ファイルアップロード');

    var ctrl = this;

    var fileSelect = function(file) {
        var f =new FormData();
        f.append('file',file);
        //post
        $http.post('./api/fileupload/upload',f,{
            transformRequest: null,
            headers: {'Content-type':undefined}
            //headers: {'Content-type':'multipart/form-data;boundary'}
        })
        .then(function(res){
            console.log(res);
        });
    }

    ctrl.submit = function(){
        var fileTag = document.getElementById("file");
        if(file.files.length > 0){
            fileSelect(file.files[0]);
        }

        return false;
    }
}

angular.module('App').controller('fileUploadController', front.controller.FileUploadController);
