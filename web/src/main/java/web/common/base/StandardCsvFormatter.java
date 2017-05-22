package web.common.base;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 基本的なCSV書式設定
 */
public class StandardCsvFormatter implements CsvFormatter {

    /**
     * ヘッダー行の取得
     * @param metaData ヘッダー情報
     * @return ヘッダー行
     */
    @Override
    public String getHeader(ResultSetMetaData metaData) throws SQLException {
        int colCount = metaData.getColumnCount();

        // ヘッダーを書き込む
        List<String> result = new ArrayList<>();
        for(int colIndex = 1;colIndex <= colCount;colIndex++) {
            result.add(String.format("\"%s\"", metaData.getColumnName(colIndex)));
        }

        return String.join(",", result);
    }

    /**
     * データ行の取得
     * @param recordSet DB取得結果
     * @param colCount カラムの数
     * @return データ行
     */
    @Override
    public String getDataRow(ResultSet recordSet, int colCount) throws SQLException {
        List<String> result = new  ArrayList<>();

        Object value = null;
        boolean isDoubleQuote = false;
        for(int colIndex = 1;colIndex <= colCount;colIndex++) {
            value = recordSet.getObject(colIndex);

            // ダブルクォーテーション囲みあり
            isDoubleQuote = true;
            if(value == null){
                value = "";
            }else{
                Class<? extends Object> valueClass = value.getClass();
                if(valueClass == Date.class) {
                    DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    value = ((Date)value).toLocalDate().format(f);
                }
                if(valueClass == Integer.class || valueClass == java.math.BigDecimal.class) {
                    isDoubleQuote = false;
                }
            }
            if(isDoubleQuote){
                result.add(String.format("\"%s\"", value));
            }
            else{
                result.add(String.format("%s", value));
            }
        }

        return String.join(",", result);
    }

}
