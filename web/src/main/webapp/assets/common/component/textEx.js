/**
 * inputタグ拡張ディレクティブ
 */
angular.module('App')
.directive('textEx', ['$compile', function($compile){
    return{
        restrict: 'A',
        scope:{
            type:'@',
            class:'@',
            ngModel:'='
        },
        link: function postLink(scope, element, attrs, ctrl){
            if(attrs.type === 'date' && !Modernizr.inputtypes.date ){
                var ngModel = element.controller('ngModel');

                ngModel.$formatters.length = 0;
                // $modelValue to $viewValue
                  ngModel.$formatters.push(function(date){
                      if(date == null || date === undefined){
                          return '';
                      }
                      return String(date.getFullYear()) + '/' +
                      ('0'+(date.getMonth()+1)).slice(-2) + '/' +
                      ('0'+date.getDate()).slice(-2);
                  });

                  // $viewValue to $modelValue
                  ngModel.$parsers.length = 0;
                  ngModel.$parsers.push(function(value){
                      if(!/^[0-9]{4}\/[0-9]?[1-9]\/[0-9]?[1-9]$/.test(value)){
                          return null;
                      }
                      return new Date(value.replace(/-/g,'/'));
                  });
            }

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

            // ime-disabledがclassに設定されていれば、半角入力制限をする
            if(!!attrs.class && attrs.class.indexOf('ime-disabled') >= 0){
                singleByteMode = true;
            }

            // スペーストリミングを無効にする
            attrs.$set('ngTrim', "false");

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
                // 半角文字以外を削除
                if(singleByteMode){
                    if(isNumberType){
                        if(newVal === undefined) {
                            if(oldVal === null){
                                // 前回の値がnullの場合は0を設定
                                newVal = 0;
                            }else{
                                // 入力値が不正な値である場合、前回の値を設定
                                newVal = oldVal;
                            }
                        }
                    }
                    if(isTextType) {
                        if(!newVal){
                            return;
                        }
                        var type = Object.prototype.toString.call(newVal).slice(8, -1).toLowerCase();
                        if(type === 'string'){
                            // 半角文字以外を削除
                            newVal = newVal.replace(/[^\s\w]/g,'').replace(/　/g,'');
                        }
                    }
                }

                // 結果を反映
                scope.ngModel = newVal;
            }
        }
    }
}]);
