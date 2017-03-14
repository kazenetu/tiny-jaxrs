/**
 * SessionStorageサービス
 */
function StorageService(){
    /**
     * SessionStorageへの保存用キー
     */
    this.keys = {
        condition:'condition',
        enableConditionPaths:'enableConditionPaths',
        updateKeys:'updateKeys'
    };

    /**
     * 値の削除
     */
    this.clearValue = function(key) {
        sessionStorage.removeItem(key);
    };

    /**
     * 値（オブジェクト）の設定
     */
    this.setValue = function(key, value) {
        sessionStorage.setItem(key, JSON.stringify(value));
    };

    /**
     * 値（オブジェクト）の取得k
     */
    this.getValue = function(key) {
        var value = sessionStorage.getItem(key);
        if (isValueNone(value)) {
            return {};
        }
        return JSON.parse(value);

    };

    /**
     * nullまたはundefinedのチェック
     */
    function isValueNone(value) {
        return value === null || value === undefined;
    }
}

angular.module('storageService',[]).service('storageService', StorageService);
