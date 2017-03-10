function PagerController() {
    var ctrl = this;

    ctrl.indexlPage = function() {
        return parseInt(ctrl.index)+1;
    }
    ctrl.prev = function() {
        if (ctrl.index > 0) {
            ctrl.index--;
            ctrl.onPaging({index:ctrl.index});
        }
    }
    ctrl.next = function() {
        if (ctrl.index < ctrl.total - 1) {
            ctrl.index++;
            ctrl.onPaging({index:ctrl.index});
        }
    }
}

myApp.component("pager", {
    bindings : {
        index : '@',
        total : '@',
        onPaging: '&'
    },
    template:`
          <div  ng-show="$ctrl.total > 0">
              <input type="button" class="btn" ng-click="$ctrl.prev()"  value="＜">
              {{$ctrl.indexlPage()}} / {{$ctrl.total}}
              <input type="button" class="btn" ng-click="$ctrl.next()"  value="＞">
          </div>
          `,
    controller : PagerController
});
