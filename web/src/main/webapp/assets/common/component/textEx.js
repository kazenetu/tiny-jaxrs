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

            // ime-disabledがclassに設定されていれば、半角入力制限をする
            if(!!attrs.class && attrs.class.indexOf('ime-disabled') >= 0){
                singleByteMode = true;
            }

            // スペーストリミングを無効にする
            attrs.$set('ngTrim', "false");

            // IE11用 日付
            if(isDateType && !Modernizr.inputtypes.date ){
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

                  //  id付与
                  var id = 'date'+scope.$id;
                  attrs.$set('id', id);

                  // 日付形式のフォーマットを設定
                  setTimeout(function(){
                      $('#'+id).FormattingTextbox("____/__/__",{
                          inputRegExp:/[0-9]/
                          ,delimiterRegExp:/[\/]/
                        });
                  },0);

                  return;
            }

            //IE11用 数値型
            if(isNumberType && !Modernizr.inputtypes.number ){
                element.bind('keydown', function (e) {
                    if(e.key!=="Backspace" && (e.char!=='' && !/[0-9.]/.test(e.char))){
                          return false;
                    }
                    return true;
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
