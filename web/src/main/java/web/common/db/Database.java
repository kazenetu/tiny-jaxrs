package web.common.db;

import java.util.List;
import java.util.Map;

/**
 * データベースインターフェース
 */
public interface Database {
    public void close();

    /**
     * トランザクション設定
     * @throws Exception 例外エラー
     */
    void setTransaction() throws Exception;

    /**
     * コミット
     * @throws Exception 例外エラー
     */
    void commit() throws Exception;

    /**
     * ロールバック
     * @throws Exception 例外エラー
     */
    void rollback() throws Exception;

    /**
     * 更新系SQL実行
     * @param sql 実行SQL
     * @param params パラメータ
     * @return 更新件数
     * @throws Exception 実行時例外エラー
     */
    int execute(String sql, List<Object> params) throws Exception;

    /**
     * 選択系SQL実行
     * @param sql 実行SQL
     * @param params パラメータ
     * @return 検索結果
     * @throws Exception 実行時例外エラー
     */
    List<Map<String,Object>> query(String sql, List<Object> params) throws Exception;
}
