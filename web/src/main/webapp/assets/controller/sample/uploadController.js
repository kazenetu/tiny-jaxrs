front.controller.UploadController = function UploadController($scope,$q, $location, webApiService, userService,storageService) {
    front.common.utils.extendController(this, front.common.controller.PageBase);
    this.setTitle('アップロードテスト');

    var ctrl = this;
    ctrl.imageName = "";
    ctrl.imageData = "";
    ctrl.uploadPath = "";
    ctrl.tag = null;

    ctrl.searchResult = null;

    /**
     * 初期化イベント
     */
    ctrl.init = function(){
        var dadArea = document.getElementById('dadArea');
        dadArea.addEventListener('dragstart',ctrl.dragStart,true);
        //dadArea.addEventListener('dragenter',ctrl.dragStart,true);
        dadArea.addEventListener('dragover',ctrl.dragOver,true);
        dadArea.addEventListener('drop',ctrl.drop,true);

        // リスト取得
        ctrl.getList();
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
              ctrl.imageName;
              $scope.$apply();
          };

          ctrl.imageName = f.name;

          // dataURLで読み込み
          reader.readAsDataURL(f);

          break;
        }
    };

    /**
     * 登録(ファイル作成)イベント
     */
    ctrl.insertOrUpdate = function(){

        var d = $q.defer();
        d.promise
        .then(ctrl.showConfirm($q,'確認',
                '登録しますか','登録する'))
        .then(function(){
            var deferrred = $q.defer();

            // データ更新
            webApiService.post('api/upload/test', {
                loginUserId: userService.getId(),
                requestData : {
                    fileName : ctrl.imageName,
                    imageData : ctrl.imageData
                }
            }, function(response) {
                if (response.result !== 'OK') {
                    ctrl.showError(response.errorMessage);
                } else {
                    ctrl.hideError();
                    ctrl.uploadPath = response.responseData;
                    ctrl.showError(response.responseData);

                    deferrred.resolve();
                }
            });

            return deferrred.promise;
        })
        .then(ctrl.showMsgDialog($q,'登録の報告',
                '登録しました', '確認'));
        // 発火
        d.resolve();
    }

    /**
     * テーブル登録
     */
    ctrl.uploadData = function(){

        var d = $q.defer();
        d.promise
        .then(ctrl.showConfirm($q,'確認',
                '登録しますか','登録する'))
        .then(function(){
            var deferrred = $q.defer();

            // データ更新
            webApiService.post('api/upload/insert', {
                loginUserId: userService.getId(),
                requestData : {
                    fileName : ctrl.imageName,
                    imageData : ctrl.imageData,
                    tag : ctrl.dateToString(ctrl.tag)
                }
            }, function(response) {
                if (response.result !== 'OK') {
                    ctrl.showError(response.errorMessage);
                } else {
                    ctrl.hideError();

                    deferrred.resolve();
                }
            })

            return deferrred.promise;
        })
        .then(ctrl.showMsgDialog($q,'登録の報告','登録しました', '確認'))
        .then(function(){
            ctrl.getList();
        });

        // 発火
        d.resolve();
    }

    /**
     * リスト取得
     */
    ctrl.getList = function(){
        // データ取得
        webApiService.post('api/upload/list', {
            loginUserId: userService.getId(),
            requestData : {
            }
        }, function(response) {
            if (response.result !== 'OK') {
                ctrl.showError(response.errorMessage);
            } else {
                ctrl.hideError();

                var resultIndex = 0;
                while(resultIndex < response.responseData.length) {
                    var item = response.responseData[resultIndex];
                    // 文字列(yyyyMMdd)からDateに変換
                    item.tag = ctrl.stringToDate(item.tag);
                    resultIndex++;
                }

                ctrl.searchResult = response.responseData;
            }
        });
    }

    /**
     * Dateから文字列(yyyyMMdd)に変換
     */
    ctrl.dateToString = function(src) {
        var type = Object.prototype.toString.call(src).slice(8, -1).toLowerCase();
        if(type === 'date'){
            return String(src.getFullYear()) +
                    ('0'+(src.getMonth()+1)).slice(-2) +
                    ('0'+src.getDate()).slice(-2);
        }

        return '';
    }

    /**
     * 文字列(yyyyMMdd)からDateに変換
     */
    ctrl.stringToDate = function(src) {
        var type = Object.prototype.toString.call(src).slice(8, -1).toLowerCase();
        if(type === 'string' && src !== ''){
            var year  = parseInt(src.substr(0,4),10);
            var month = parseInt(src.substr(4,2),10) - 1;
            var day   = parseInt(src.substr(6,2),10);
            return new Date(year, month, day);
        }

        return null;
    }

}

angular.module('App').controller('uploadController', front.controller.UploadController);
