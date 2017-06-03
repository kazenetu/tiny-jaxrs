package web.common.db;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import web.common.util.EnvironmentUtil;

/**
 * データベースインターフェース
 */
public interface Database {
    public void close() throws Exception;

    /*
     * フェッチサイズを設定
     * @param rows 行サイズ
     */
    void setFetchSize(int rows);

    /**
     * コネクション取得
     * @return コネクション
     */
    Connection getConnection();

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

    /**
     * SQL発行ログを出力
     * @param logger ログインターフェース
     * @param sql 実行SQL
     * @param params パラメータ
     * @throws Exception
     */
    default void writeSqlLog(Logger logger, String sql, List<Object> params) throws Exception {
        // ログ出力対象か否か
        if(!EnvironmentUtil.getInstance().isOutputSqlLog()) {
            return;
        }

        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];

        logger.info("呼び出し元[{}] SQL[{}] params[{}]",
                stackTraceElement.getClassName() + "#" + stackTraceElement.getMethodName(),
                sql,params);
    }
}
