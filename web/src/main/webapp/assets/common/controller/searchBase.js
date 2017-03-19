/**
 * 検索画面のベース画面
 */
front.common.controller.SearchBase = function SearchBase(){
    extendController(this,front.common.controller.PageBase);

    var ctrl = this;

    ctrl.childlen = [];

    /**
     * ページコントロールから呼び出し元に自身のインスタンスを登録
     */
    ctrl.sendRoot = function(src) {
        if (ctrl.childlen.indexOf(src) == -1) {
            ctrl.childlen.push(src);
        }
    }

    /**
     * ページング処理
     */
    ctrl.paging = function(pageIndex, sender) {
        ctrl.getPage(pageIndex);

        ctrl.sendPageIndex(pageIndex, sender);
    }

    /**
     * 更新後のページ数をほかのページンコントロールに通知
     */
    ctrl.sendPageIndex = function(pageIndex, sender) {
        var index = 0;
        while (index < ctrl.childlen.length) {
            if (ctrl.childlen[index] !== sender) {
                ctrl.childlen[index].setPagetIndex(pageIndex);
            }
            index++;
        }
    }

}