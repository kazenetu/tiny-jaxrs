function PagerController() {
    var ctrl = this;

    ctrl.pageIndex = 0;

    ctrl.indexlPage = function() {
        return parseInt(ctrl.pageIndex)+1;
    }
    ctrl.prev = function() {
        if (ctrl.isEnabledPrev()) {
            ctrl.pageIndex --;
            ctrl.onPaging({pageIndex:ctrl.pageIndex,sender:ctrl });
        }
    }
    ctrl.next = function() {
        if (ctrl.isEnabledNext()) {
            ctrl.pageIndex ++;
            ctrl.onPaging({pageIndex:ctrl.pageIndex,sender:ctrl });
        }
    }

    ctrl.isEnabledPrev = function(){
        return ctrl.pageIndex > 0;
    }
    ctrl.isEnabledNext = function(){
        return ctrl.pageIndex  < ctrl.totalPage - 1;
    }

    ctrl.$onInit = function() {
        ctrl.onSendRoot({src:ctrl});
    };

    ctrl.setPagetIndex = function(index){
        ctrl.pageIndex = index;
    }

}

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
