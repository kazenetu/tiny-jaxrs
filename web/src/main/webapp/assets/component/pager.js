/**
 * ページャーコンポーネント
 */
function PagerController() {
    var ctrl = this;

    /**
     * 現在ページ
     */
    ctrl.pageIndex = 0;

    /**
     * 表示用現在ページ
     */
    ctrl.indexlPage = function() {
        return parseInt(ctrl.pageIndex)+1;
    }

    /**
     * 前ページボタンクリック
     */
    ctrl.prev = function() {
        if (ctrl.isEnabledPrev()) {
            ctrl.pageIndex --;
            ctrl.onPaging({pageIndex:ctrl.pageIndex,sender:ctrl });
        }
    }

    /**
     * 次ページボタンクリック
     */
    ctrl.next = function() {
        if (ctrl.isEnabledNext()) {
            ctrl.pageIndex ++;
            ctrl.onPaging({pageIndex:ctrl.pageIndex,sender:ctrl });
        }
    }

    /**
     * 前ページボタン有効状態取得
     */
    ctrl.isEnabledPrev = function(){
        return ctrl.pageIndex > 0;
    }

    /**
     * 次ページボタン有効状態取得
     */
    ctrl.isEnabledNext = function(){
        return ctrl.pageIndex  < ctrl.totalPage - 1;
    }

    /**
     * ロード完了イベント
     */
    ctrl.$onInit = function() {
        // 呼び出し元にインスタンスを登録
        ctrl.onSendRoot({src:ctrl});
    };

    /**
     * 現在ページ変更イベント
     * （呼び出しもとから呼び出される）
     */
    ctrl.setPagetIndex = function(index){
        ctrl.pageIndex = index;
    }

}

/**
 * コンポーネントを登録
 */
myApp.component("pager", {
    bindings : {
        totalPage : '@',
        onPaging: '&',
        onSendRoot:'&'
    },
    template:`
          <div  ng-show="$ctrl.totalPage > 0">
              <input type="button" class="btn" ng-click="$ctrl.prev()" ng-disabled="!$ctrl.isEnabledPrev()"  value="＜">
              {{$ctrl.indexlPage()}} / {{$ctrl.totalPage}}
              <input type="button" class="btn" ng-click="$ctrl.next()" ng-disabled="!$ctrl.isEnabledNext()" value="＞">
          </div>
          `,
    controller : PagerController
});
