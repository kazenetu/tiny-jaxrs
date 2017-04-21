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
