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
    ctrl.showError = function(message){
        if(ctrl.header === null) return;

        ctrl.header.showError(message);
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
        document.title = 'Sample:' + title;
    }

    /**
     * 確認メッセージの表示
     */
    ctrl.showConfirm = function(title,message,buttonText,callFunction){
        if(ctrl.header === null) return;

        ctrl.header.showConfirm(title,message,buttonText,callFunction);
    }

    /**
     * メッセージダイアログの表示
     */
    ctrl.showMsgDialog = function(title,message,buttonText,callFunction){
        if(ctrl.header === null) return;

        ctrl.header.showMsgDialog(title,message,buttonText,callFunction);
    }
}