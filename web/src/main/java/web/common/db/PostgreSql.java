package web.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostgreSqlアクセスクラス
 *
 */
public class PostgreSql implements Database {
    private static Logger logger = LoggerFactory.getLogger(PostgreSql.class);

    private Connection con = null;
    private boolean isSetTransaction = false;
    private int fetchSize = -1;
    private boolean isCreateConnection = false;

    public PostgreSql(Connection connection){
        if(connection == null) {
            isCreateConnection = true;

            // context.xmlからDataSourceを取得
            try {
                InitialContext context = new InitialContext();
                DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/PostgreSql");
                con = ds.getConnection();
                con.setAutoCommit(false);
                context.close();
                return;
            } catch (Exception e1) {
                logger.debug("外部ファイルからコネクション取得失敗："+e1.getMessage());
            }

            logger.debug("デバッグ用コネクションを取得");

            // 取得できない場合はリソースパスから取得
            try {
                Class.forName("org.postgresql.Driver");
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testDB","test","test");
                con.setAutoCommit(false);
            } catch (Exception e) {
                logger.debug("デバッグ用コネクション取得失敗："+e.getMessage());
            }
        }
        else{
            con = connection;
        }

    }

    @Override
    public void close() throws Exception {
        if (con != null && isCreateConnection) {
            try {
                if (isSetTransaction) {
                    con.rollback();
                }
                con.close();
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
    }

    /**
     * フェッチサイズを設定
     * @param rows 行サイズ
     */
    public void setFetchSize(int rows){
        if(!isCreateConnection){
            return;
        }
        fetchSize = rows;
    }

    /**
     * コネクション取得
     * @return コネクション
     */
    public Connection getConnection(){
        return con;
    }

    /**
     * トランザクション設定
     * @throws Exception 例外エラー
     */
    public void setTransaction() throws Exception {
        if(!isCreateConnection){
            return;
        }
        try {
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
        if(!isCreateConnection){
            return;
        }
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
        if(!isCreateConnection){
            return;
        }
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
        if(!isSetTransaction && isCreateConnection) {
            throw new Exception("setTransactionメソッドが実行されていません");
        }

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

            // フェッチサイズが設定されていれば設定
            if(fetchSize > 0){
                statement.setFetchSize(fetchSize);
            }

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
