package web.common.util;

/**
 * 例外エラーとして扱わない例外エラーをチェックするユーティリティ
 */
public final class ExcludedExceptionUtil {

    /**
     * ブラウザがダウンロードをキャンセルした際に発生する例外エラーか否か(tomcat用)
     * @param e 対象の例外エラー
     * @return 例外エラーか否か
     */
    public static boolean isClientAbortException(Exception e) {
        return "org.apache.catalina.connector.ClientAbortException".equals(e.getClass().getName());
    }
}
