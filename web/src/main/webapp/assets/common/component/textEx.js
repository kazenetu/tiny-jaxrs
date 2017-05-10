/**
 * inputタグ拡張ディレクティブ
 */
angular.module('App')
.directive('textEx', [function(){
    return{
        restrict: 'A',
        scope:{
            type:'@',
            class:'@',
            ngModel:'='
        },
        link: function postLink(scope, element, attrs, ctrl){

            /**
             * 半角文字のみ許可するか否か
             */
            var singleByteMode = false;

            /**
             * typeが'text'か否か
             */
            var isTextType = attrs.type === 'text';

            /**
             * typeが'number'か否か
             */
            var isNumberType = attrs.type === 'number';

            /**
             * typeが'date'か否か
             */
            var isDateType = attrs.type === 'date';

            /**
             * typeが'time'か否か
             */
            var isTimeType = attrs.type === 'time';

            /**
             * クリアボタンを表示するか否か
             */
            var isShowClearButton = false;

            //  id付与
            var id = 'scope'+scope.$id;
            attrs.$set('id', id);

            // ime-disabledがclassに設定されていれば、半角入力制限をする
            if(!!attrs.class && attrs.class.indexOf('ime-disabled') >= 0){
                singleByteMode = true;
            }

            // スペーストリミングを無効にする
            attrs.$set('ngTrim', "false");

            // date有効ブラウザ用
            if(isDateType && Modernizr.inputtypes.date) {
                // 最大日付を設定
                attrs.$set('max', "2100-12-31");

                // edgeの場合はクリアボタンを追加
                if(navigator.userAgent.indexOf('Edge') >= 0) {
                    isShowClearButton = true;
                }
            }

            // time有効ブラウザ用
            if(isTimeType && Modernizr.inputtypes.time) {
                // ミリ秒まで入力できるようにする
                attrs.$set('step', "1");

                // edgeの場合はクリアボタンを追加
                if(navigator.userAgent.indexOf('Edge') >= 0) {
                    isShowClearButton = true;
                }
            }

            // クリアボタンを追加
            if(isShowClearButton) {
                var style = 'margin-left:-1em;cursor:pointer;';
                if(attrs.class.indexOf('form-control') >= 0) {
                    style += 'margin-top:0.5em;position:absolute;';
                    $('#'+id).css('float','left');
                }

                // クリアアイコンを追加
                $('#'+id).after('<a style="' + style + '" id="b_' + id + '">X</a>');
                $('#b_'+id).on('click',function(){
                    $('#'+id).val(null);
                });
            }

            // 日付用
            if(isDateType){
                // IE11用
                if(!Modernizr.inputtypes.date) {
                    var ngModel = element.controller('ngModel');

                    ngModel.$formatters.length = 0;
                    // $modelValue to $viewValue
                      ngModel.$formatters.push(function(date){
                          if(date === null || date === undefined){
                              return '____/__/__';
                          }
                          var dateString = dateToString(date);

                          $('#'+id).trigger('datachange',[dateString]);

                          return dateString;
                      });

                      // $viewValue to $modelValue
                      ngModel.$parsers.length = 0;
                      ngModel.$parsers.push(function(value){
                          if(value === '____/__/__'){
                              return null;
                          }

                          if(!/^[0-9]{4}\/[0-9]?[0-9]\/[0-9]?[0-9]$/.test(value)){
                              return undefined;
                          }
                          var result = new Date(value.replace(/-/g,'/'));
                          if(result.toString() === 'Invalid Date'){
                              return undefined;
                          }

                          var dateString = dateToString(result);
                          if(value !== dateString){
                              result = new Date(result.getFullYear(),result.getMonth(),0);
                              dateString = dateToString(result);
                              $('#'+id).trigger('datachange',[dateString]);
                          }
                          return result;
                      });

                      // 日付形式のフォーマットを設定
                      setTimeout(function(){
                          $('#'+id).FormattingTextbox("____/__/__",{
                              inputRegExp:/[0-9]/
                              ,delimiterRegExp:/[\/]/
                          });
                          var dateString = dateToString(scope.ngModel);
                          if(dateString !== ''){
                              $('#'+id).trigger('datachange',[dateString]);
                          }
                      },0);

                      /**
                       * Date型を文字列に変換
                       */
                      function dateToString(date) {
                          if(date == null || date === undefined){
                              return '';
                          }
                          return ('0'+String(date.getFullYear())).slice(-4) + '/' +
                                  ('0'+(date.getMonth()+1)).slice(-2) + '/' +
                                  ('0'+date.getDate()).slice(-2);
                      }

                      return;
                }
                // type=date有効ブラウザ用ロストフォーカスイベント
                element.bind('blur', function (e) {
                    // 入力OKでngModelがエラーの場合はnullを設定
                    if($('#'+id)[0].checkValidity() && scope.ngModel === undefined) {
                        scope.ngModel = null;
                    }
                });
            }

            // IE11用時刻
            if(isTimeType && !Modernizr.inputtypes.time) {
                var ngModel = element.controller('ngModel');

                ngModel.$formatters.length = 0;
                // $modelValue to $viewValue
                  ngModel.$formatters.push(function(time){
                      if(time === null || time === undefined){
                          return '__:__:__';
                      }
                      var timeString = timeToString(time);

                      $('#'+id).trigger('datachange',[timeString]);

                      return timeString;
                  });

                  // $viewValue to $modelValue
                  ngModel.$parsers.length = 0;
                  ngModel.$parsers.push(function(value){
                      if(value === '__:__:__'){
                          return null;
                      }

                      if(!/^[0-9]{2}:[0-9]{2}:[0-9]{2}$/.test(value)){
                          return undefined;
                      }
                      var result = new Date('1970/01/01 ' + value);
                      if(result.toString() === 'Invalid Date'){
                          return undefined;
                      }

                      var timeString = timeToString(result);
                      $('#'+id).trigger('datachange',[timeString]);

                      return result;
                  });

                  // 時刻形式のフォーマットを設定
                  setTimeout(function(){
                      $('#'+id).FormattingTextbox("__:__:__",{
                          inputRegExp:/[0-9]/
                          ,delimiterRegExp:/[:]/
                      });
                      var timeString = timeToString(scope.ngModel);
                      if(timeString !== ''){
                          $('#'+id).trigger('datachange',[timeString]);
                      }
                  },0);

                  /**
                   * Date型を文字列に変換
                   */
                  function timeToString(time) {
                      if(time == null || time === undefined){
                          return '';
                      }
                      return ('0'+(time.getHours())).slice(-2) + ':' +
                              ('0'+time.getMinutes()).slice(-2) + ':' +
                              ('0'+time.getSeconds()).slice(-2);
                  }

                  return;
            }

            //数値型用イベント
            if(isNumberType) {
                // キーダウン：特殊キーと数値以外は入力不可
                element.bind('keydown', function (e) {
                    if(e.key.length <= 1 && !/[0-9.]/.test(e.key)){
                          return false;
                    }
                    return true;
                });
                // フォーカスロスト：整数部の不要な0を排除
                element.bind('blur', function (e) {
                    if(scope.ngModel === null){
                        return;
                    }
                    var value = $(element).val();
                    var values = value.split('.');

                    //整数部を数値化→文字列化
                    value = Number(values[0]).toString();
                    if(values.length > 1){
                        // 小数部があればそのまま追加
                        value += '.' + values[1];
                    }
                    $(element).val(value);
                });
            }

            /**
             * フォーカスロスト時 値変更イベント
             */
            element.bind('change', function () {
                // 入力あり かつ textの場合は後ろスペース除去
                if(!!scope.ngModel && isTextType){
                    // 後ろのスペースを除去
                    scope.ngModel = scope.ngModel.replace(/[\s]+$/g,'')
                }
            });

            // 値変更イベント
            if (attrs.ngModel) {
                scope.$parent.$watch(attrs.ngModel, textChange);
            }

            /**
             * 値変更
             */
            function textChange(newVal, oldVal){
                // type=date
                if(isDateType){
                    if(newVal === undefined && !!oldVal){
                        newVal = new Date(oldVal.getFullYear(),oldVal.getMonth()+1,0);
                    }
                }

                // type=number
                if(isNumberType){
                    if(newVal === null || newVal === undefined) {
                        if(newVal === undefined && oldVal !== null){
                            // 入力値が不正な値である場合、前回の値を設定
                            newVal = oldVal;
                        }else{
                            // nullを設定
                            newVal = null;
                        }
                    }
                }

                // 半角文字以外を削除
                if(singleByteMode){
                    if(isTextType) {
                        if(!newVal){
                            return;
                        }
                        var type = Object.prototype.toString.call(newVal).slice(8, -1).toLowerCase();
                        if(type === 'string'){
                            // 半角文字以外を削除
                            newVal = newVal.replace(/[^\s\w-]/g,'').replace(/　/g,'');
                        }
                    }
                }

                // 結果を反映
                scope.ngModel = newVal;
            }
        }
    }
}]);
