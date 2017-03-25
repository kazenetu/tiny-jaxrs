package web.common.base;

import java.util.Map;

import web.common.db.Database;
import web.common.db.PostgreSql;
import web.common.db.Sqlite;

/**
 * モデルクラスのスーパークラス
 */
public abstract class Model implements AutoCloseable {
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
     * コンストラクタ
     */
    public Model() {

        // dbの種類を設定
        String dbName = System.getenv("DBNAME");

        if (dbName == null || "".equals(dbName)) {
            dbName = DBType.SQLite.toString();
        }

        // dbのインスタンスを作成
        if (DBType.SQLite.toString().equals(dbName)) {
            db = new Sqlite();
            columnType = ColumnType.UPPER;
            return;
        }
        if (DBType.PostgreSQL.toString().equals(dbName)) {
            db = new PostgreSql();
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
}
