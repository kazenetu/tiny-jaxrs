/**
 * ページベース
 */
front.common.controller.PageBase = function PageBase(){
    var ctrl = this;

    ctrl.title = '';
    ctrl.header = null;

    /**
     * ヘッダーコントロールから呼び出し元に自身のインスタンスを登録
     */
    ctrl.sendRootHeader = function(src){
        ctrl.header = src;

        // タイトル設定
        ctrl.header.setTitle(ctrl.title);
    }

    /**
     * エラーメッセージの表示
     */
    ctrl.showError = function(message, params){
        if(ctrl.header === null) return;

        // メッセージ取得
        var argParam = params;
        if(!params){
            argParam = [];
        }
        var formattedMessage = getMessage(message, argParam);
        ctrl.header.showError(formattedMessage);
    }

    /**
     * エラーメッセージの非表示
     */
    ctrl.hideError = function(){
        if(ctrl.header === null) return;

        ctrl.header.hideError();
    }

    /**
     * タイトル設定
     */
    ctrl.setTitle = function(title){
        ctrl.title = title;
        document.title = title;
    }

    /**
     * 確認メッセージの表示
     */
    ctrl.showConfirm = function($q,title,message,buttonText,params){
        if(ctrl.header === null) return;

        return function(){
            var deferrred = $q.defer();

            // メッセージ取得
            var argParam = params;
            if(!params){
                argParam = [];
            }
            var formattedMessage = getMessage(message, argParam);

            // タイトル取得
            var formattedTitle = getMessage(title, []);

            ctrl.header.showConfirm(formattedTitle,formattedMessage,buttonText,function(){
                deferrred.resolve();
            });

            return deferrred.promise;
        }
    }

    /**
     * メッセージダイアログの表示
     */
    ctrl.showMsgDialog = function($q,title,message,buttonText,params){
        if(ctrl.header === null) return;

        return function(){
            var deferrred = $q.defer();

            // メッセージ取得
            var argParam = params;
            if(!params){
                argParam = [];
            }
            var formattedMessage = getMessage(message, argParam);

            // タイトル取得
            var formattedTitle = getMessage(title, []);

            ctrl.header.showMsgDialog(formattedTitle,formattedMessage,buttonText,function(){
                deferrred.resolve();
            });

            return deferrred.promise;
        }
    }

    /**
     * メッセージ取得
     */
    function getMessage(messageId,params){
        var message = messageId;
        if(messageId in front.common.messages){
            var message = front.common.messages[messageId];

            var index=0;
            while(index < params.length){
                message = message.replace('{'+index+'}', params[index]);
                index++;
            }
        }
        return message;
    }

    /**
     * 最大桁数超過チェック
     */
    ctrl.isOverMaxLength = function(src, maxLength) {
        if(!src){
            return false;
        }
        return src.toString().length > maxLength;
    }

    /**
     * 英数字チェック
     */
    ctrl.isNumAlpha = function(src) {
        if(!src){
            return false;
        }
        return /^[0-9A-Za-z]+$/.test(src);
    }

    /**
     * 英字チェック
     */
    ctrl.isAlpha = function(src) {
        if(!src){
            return false;
        }
        return /^[A-Za-z]+$/.test(src);
    }

    /**
     * カタカナチェック
     */
    ctrl.isKana = function(src) {
        if(!src){
            return false;
        }
        return /^[ｱ-ﾝ|ﾞｰ|ァ-ンヴー| |　]+$/.test(src);
    }

    /**
     * 数値チェック
     *  @param {string} {src} - 入力値
     *  @param {number} {intPartCount} - 整数部の桁数
     *  @param {number} [decimalPartCount=0] - 小数部の桁数(省略可能)
     */
    ctrl.isNumber = function(src, intPartCount, decimalPartCount) {
        if(src === null || src === undefined) {
            return true;
        }

        if(decimalPartCount === null || decimalPartCount === undefined) {
            decimalPartCount = 0;
        }
        var regString = '';
        var count;
        // 整数部
        if(intPartCount>1){
            regString += '[1-9]?';
            intPartCount -= 1;
        }
        regString += '[0-9]{1,' + intPartCount + '}';

        // 小数部
        if(decimalPartCount > 0){
            regString += '[.]?[0-9]{0,' + decimalPartCount + '}';
            for(count=decimalPartCount;count>0;count--){
            }
        }

        return new RegExp('^'+ regString + '$').test(src);
    }
}