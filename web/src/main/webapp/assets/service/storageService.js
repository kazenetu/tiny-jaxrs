function StorageService(){
    this.keys = {
        condition:'condition',
        updateKeys:'updateKeys'
    };
    this.KEY_CONDITION = 'searchCondition';
    this.KEY_EDIT_DATA = 'editData';

    this.setValue = function(key, value) {
        sessionStorage.setItem(key, value);
    };

    this.getValue = function(key) {
        var value = sessionStorage.getItem(key);
        if (isValueNone(value)) {
            value = "";
        }
        return value;

    };

    function isValueNone(value) {
        return value === null || value === undefined;
    }
}

angular.module('storageService',[]).service('storageService', StorageService);
