front.controller.FileUploadController =  function FileUploadController($location, webApiService, userService,storageService,$http) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('ファイルアップロード');

    var ctrl = this;

    var converBase64 = function(file) {
        var fileData = new FormData();
        fileData.append('file',file);

        //post
        $http.post('./api/fileupload/convert/base64',fileData,{
            transformRequest: null,
            headers: {'Content-type':undefined}
        })
        .then(function(res){
            console.log(res.data);
        });
    }

    ctrl.submit = function(){
        var fileTag = document.getElementById("file");
        if(file.files.length > 0){
            converBase64(file.files[0]);
        }

        return false;
    }
}

angular.module('App').controller('fileUploadController', front.controller.FileUploadController);
