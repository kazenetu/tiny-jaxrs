package web.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.sqlite.SQLiteDataSource;

/**
 * SQLiteアクセスクラス
 * @author nayaon
 *
 */
public class Sqlite implements Database {
    private Connection con = null;
    private boolean isSetTransaction = false;

    public Sqlite() {

        // context.xmlからDataSourceを取得
        try {
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/SQLite");
            con = ds.getConnection();
            context.close();
            return;
        } catch (Exception e1) {
            // TODO 自動生成された catch ブロック
            e1.printStackTrace();
        }

        // 取得できない場合はリソースパスから取得
        try {
            //クラスローダーからdbファイルの物理パスを取得する
            String filePath = this.getClass().getClassLoader().getResource("test.db").getPath();

            // データベースに接続する なければ作成される
            SQLiteDataSource sqliteDs = new SQLiteDataSource();
            sqliteDs.setUrl("jdbc:sqlite:" + filePath);

            con = sqliteDs.getConnection();

        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            // Connection の例外が発生した時

            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (con != null) {
            try {
                if (isSetTransaction) {
                    con.rollback();
                }
                con.close();
            } catch (SQLException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
        }
    }

    /**
     * トランザクション設定
     * @throws Exception 例外エラー
     */
    public void setTransaction() throws Exception {
        try {
            con.setAutoCommit(false);
            con.setSavepoint();
            isSetTransaction = true;
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    /**
     * コミット
     * @throws Exception 例外エラー
     */
    public void commit() throws Exception {
        try {
            con.commit();
            isSetTransaction = false;
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    /**
     * ロールバック
     * @throws Exception 例外エラー
     */
    public void rollback() throws Exception {
        try {
            con.rollback();
            isSetTransaction = false;
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    /**
     * 更新系SQL実行
     * @param sql 実行SQL
     * @param params パラメータ
     * @return 更新件数
     * @throws Exception 実行時例外エラー
     */
    public int execute(String sql, List<Object> params) throws Exception {
        try (PreparedStatement statement = con.prepareStatement(sql);) {

            int i = 1;
            for (Object param : params) {
                statement.setObject(i, param);
                i++;
            }

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    /**
     * 選択系SQL実行
     * @param sql 実行SQL
     * @param params パラメータ
     * @return 検索結果
     * @throws Exception 実行時例外エラー
     */
    public List<Map<String,Object>> query(String sql, List<Object> params) throws Exception {
        List<Map<String,Object>> resultList = new ArrayList<>();

        try (PreparedStatement statement = con.prepareStatement(sql);) {

            int i = 1;
            for (Object param : params) {
                statement.setObject(i, param);
                i++;
            }

            ResultSet result = statement.executeQuery();
            ResultSetMetaData metaData = result.getMetaData();
            int colCount = metaData.getColumnCount();

            while(result.next()){
                Map<String,Object> recodeMap = new HashMap<>();
                for(i=1;i<=colCount;i++){
                    recodeMap.put(metaData.getColumnName(i), result.getObject(i));
                }
                resultList.add(recodeMap);
            }

        } catch (SQLException e) {
            throw new Exception(e);
        }
        return resultList;
    }
}
