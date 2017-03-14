function StorageService(){
    this.keys = {
        condition:'condition',
        enableConditionPaths:'enableConditionPaths',
        updateKeys:'updateKeys'
    };
    this.KEY_CONDITION = 'searchCondition';
    this.KEY_EDIT_DATA = 'editData';

    this.clearValue = function(key) {
        sessionStorage.removeItem(key);
    };

    this.setValue = function(key, value) {
        sessionStorage.setItem(key, JSON.stringify(value));
    };

    this.getValue = function(key) {
        var value = sessionStorage.getItem(key);
        if (isValueNone(value)) {
            return {};
        }
        return JSON.parse(value);

    };

    function isValueNone(value) {
        return value === null || value === undefined;
    }
}

angular.module('storageService',[]).service('storageService', StorageService);
