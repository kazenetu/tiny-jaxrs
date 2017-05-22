package web.common.base;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * CSV書式設定インターフェース
 */
public interface CsvFormatter {
    /**
     * ヘッダー行の取得
     * @param metaData ヘッダー情報
     * @return ヘッダー行
     */
    String getHeader(ResultSetMetaData metaData) throws SQLException;

    /**
     * データ行の取得
     * @param recordSet DB取得結果
     * @param colCount カラムの数
     * @return データ行
     */
    String getDataRow(ResultSet recordSet,int colCount) throws SQLException;
}
