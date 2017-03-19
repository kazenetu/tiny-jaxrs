package web.common.base;

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
     * データベースインターフェース
     */
    protected Database db;

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
            return;
        }
        if (DBType.PostgreSQL.toString().equals(dbName)) {
            db = new PostgreSql();
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
}
