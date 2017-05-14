package web.common.base;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import web.common.db.Database;
import web.common.db.PostgreSql;
import web.common.db.Sqlite;

/**
 * モデルクラスのスーパークラス
 */
public abstract class Model implements AutoCloseable {

    /**
     * 1ページあたりのレコード数
     */
    protected final int PAGE_COUNT = 20;

    /**
     * DBの種類
     */
    private enum DBType {
        SQLite, PostgreSQL;
    }

    /**
     * カラムタイプ
     */
    private enum ColumnType{
        UPPER,LOWER
    }

    /**
     * データベースインターフェース
     */
    protected Database db;

    /**
     * カラムタイプ
     */
    private ColumnType columnType;

    /**
     * コンストラクタ(コネクション作成)
     */
    public Model() {
        initialize(null);
    }

    /**
     * コンストラクタ(コネクション引き継ぎ)
     * @param mainModel メインModel
     */
    public Model(Model mainModel){
        initialize(mainModel.db.getConnection());
    }

    /**
     * 初期化
     */
    private void initialize(Connection connection) {
        // dbの種類を設定
        String dbName = System.getenv("DBNAME");

        if (dbName == null || "".equals(dbName)) {
            dbName = DBType.SQLite.toString();
        }

        // dbのインスタンスを作成
        if (DBType.SQLite.toString().equals(dbName)) {
            db = new Sqlite(connection);
            columnType = ColumnType.UPPER;
            return;
        }
        if (DBType.PostgreSQL.toString().equals(dbName)) {
            db = new PostgreSql(connection);
            columnType = ColumnType.LOWER;
            return;
        }
    }

    /**
     * クローズメソッド
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        db.close();
        db = null;
    }

    /**
     * パラメータの未入力チェック
     * @param value 文字列インスタンス
     * @return nullまたは空文字の場合はtrue、値が入っている場合はfalse
     */
    public boolean isNullorEmpty(String value){
        return (value == null || value.length() == 0);
    }

    /**
     * カラム値取得
     * @param row レコードオブジェクト
     * @param columnName カラム名(大文字・小文字どちらでもOK)
     * @return カラム値
     */
    protected Object getColumnValue(Map<String,Object> row,String columnName){
        String key="";
        if(columnType == ColumnType.UPPER){
            key = columnName.toUpperCase();
        }else{
            key = columnName.toLowerCase();
        }
        return row.get(key);
    }

    /**
     * CSV出力用カラムヘッダ取得
     * @param metaData 列情報
     * @return カラムへヘッダをカンマ区切りにした文字列
     * @throws SQLException
     */
    protected String getCsvHeaderColumn(ResultSetMetaData metaData) throws SQLException {
        int colCount = metaData.getColumnCount();

        // ヘッダーを書き込む
        List<String> result = new ArrayList<>();
        for(int colIndex = 1;colIndex <= colCount;colIndex++) {
            result.add(String.format("\"%s\"", metaData.getColumnName(colIndex)));
        }

        return String.join(",", result);
    }

    /**
     * CSV出力用カンマ区切り文字列取得
     * @param recordSet DBから取得したRecordSet
     * @param colCount カラム数
     * @return カンマ区切り文字列取得
     * @throws SQLException
     */
    protected String getCsvColumnValue(ResultSet recordSet,int colCount) throws SQLException {
        List<String> result = new  ArrayList<>();

        Object value = null;
        boolean isDoubleQuote = false;
        for(int colIndex = 1;colIndex <= colCount;colIndex++) {
            value = recordSet.getObject(colIndex);

            // ダブルクォーテーション囲みあり
            isDoubleQuote = true;
            if(value != null){
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

    /**
     * レコード数から総ページ数を取得
     * @param recordCount レコード数
     * @return 総ページ数(レコード数がゼロ件の場合は-1)
     */
    protected int getTotalPageCount(int recordCount){
        int pageCount = -1;
        if(recordCount > 0){
            pageCount = recordCount / PAGE_COUNT;
            if(recordCount <= PAGE_COUNT){
                pageCount = 0;
            }else{
                if(recordCount - pageCount*PAGE_COUNT > 0){
                    pageCount++;
                }
            }
        }
        return pageCount;
    }
}
