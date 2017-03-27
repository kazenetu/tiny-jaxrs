package web.resource;

/**
 * メッセージ定数クラス
 */
public final class MessagesConst {
    private MessagesConst(){

    }

    /**
     * エラーメッセージ
     */
    public static class ErrorCodes {
        /**
         * ログインエラー
         */
        public static String LOGIN = "E0001";

        /**
         * パスワード変更エラー
         */
        public static String PASSWORD_CHANGE = "E0002";

        /**
         * データ取得エラー
         */
        public static String NOT_FOUND = "E0003";

        /**
         * 登録エラー
         */
        public static String INSERT = "E0010";

        /**
         * 更新エラー
         */
        public static String UPDATE = "E0011";

        /**
         * 削除エラー
         */
        public static String DELETE = "E0012";
    }

    /**
     * 警告メッセージ
     */
    public static class WarnCodes {
        /**
         * 検索結果0件
         */
        public static String SEARCH_RESULT_ZERO= "W0001";
    }
}
