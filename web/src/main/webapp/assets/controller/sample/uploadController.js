front.controller.UploadController = function UploadController($scope,$q, $location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('アップロードテスト');

    var ctrl = this;
    ctrl.imageData = "";

    /**
     * 初期化イベント
     */
    ctrl.init = function(){
        var dadArea = document.getElementById('dadArea');
        dadArea.addEventListener('dragstart',ctrl.dragStart,true);
        //dadArea.addEventListener('dragenter',ctrl.dragStart,true);
        dadArea.addEventListener('dragover',ctrl.dragOver,true);
        dadArea.addEventListener('drop',ctrl.drop,true);
    }

    ctrl.dragStart = function(event){
        event.dataTransfer.setData('image/jpeg',event.target.id);
        event.dataTransfer.setData('image/png',event.target.id);
        //event.dataTransfer.setData('text/plain',event.target.id);
    };

    ctrl.dragOver = function(event){
        event.stopPropagation();
        event.preventDefault();
        event.dataTransfer.dropEffect = 'copy';
    };

    ctrl.drop = function(event){
        event.preventDefault();

        var files = event.dataTransfer.files;

        for (var i = 0, f; f = files[i]; i++) {

          // 画像ファイルだけ
          if (!f.type.match('image.*')) {
            continue;
          }

          var reader = new FileReader();

          // ファイルロード完了
          reader.onload = function(e) {
              //resultImageにイメージ表示
              ctrl.imageData = e.target.result;
              $scope.$apply();
          };

          // dataURLで読み込み
          reader.readAsDataURL(f);
        }
    };

    /**
     * 登録または更新イベント
     */
    ctrl.insertOrUpdate = function(){

        var d = $q.defer();
        d.promise
        .then(ctrl.showConfirm($q,ctrl.commmitButtonName + 'の確認',
                ctrl.commmitButtonName +'しますか', ctrl.commmitButtonName +'する'))
        .then(function(){
            var deferrred = $q.defer();

            var apiUrl = settings.updateApiUrl;
            if(settings.isCreateMode()){
                apiUrl = settings.insertApiUrl;
            }

            // データ更新
            webApiService.post(apiUrl, {
                loginUserId: userService.getId(),
                requestData : settings.getInsUpdRequestData()
            }, function(response) {
                if (response.result !== 'OK') {
                    ctrl.showError(response.errorMessage);
                } else {
                    ctrl.hideError();

                    deferrred.resolve();
                }
            });

            return deferrred.promise;
        })
        .then(ctrl.showMsgDialog($q,ctrl.commmitButtonName + 'の報告',
                 ctrl.commmitButtonName +'しました', '確認'))
        .then(function(){
            $location.path(settings.listPage);
            storageService.clearValue(storageService.keys.updateKeys);
        });
        // 発火
        d.resolve();
    }

}

angular.module('App').controller('uploadController', front.controller.UploadController);
