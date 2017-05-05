package web.common.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

/**
 * SQLiteアクセスクラス
 *
 */
public class Sqlite implements Database {
    private static Logger logger = LoggerFactory.getLogger(Sqlite.class);

    private Connection con = null;
    private boolean isSetTransaction = false;
    private int fetchSize = -1;
    private boolean isCreateConnection = false;

    public Sqlite(Connection connection) {
        if(connection == null) {
            isCreateConnection = true;
            // context.xmlからDataSourceを取得
            try {
                InitialContext context = new InitialContext();
                DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/SQLite");
                con = ds.getConnection();
                context.close();
                return;
            } catch (Exception e1) {
                logger.debug("外部ファイルからコネクション取得失敗："+e1.getMessage());
            }

            logger.debug("デバッグ用コネクションを取得");

            // 取得できない場合はリソースパスから取得
            try {
                //クラスローダーからdbファイルの物理パスを取得する
                String filePath = this.getClass().getClassLoader().getResource("test.db").getPath();

                // データベースに接続する なければ作成される
                SQLiteDataSource sqliteDs = new SQLiteDataSource();
                sqliteDs.setUrl("jdbc:sqlite:" + filePath);

                con = sqliteDs.getConnection();

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
            } catch (SQLException e) {
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
        try (PreparedStatement statement = con.prepareStatement(sql);) {

            int i = 1;
            for (Object param : params) {
                Object value = param;
                boolean isChangedString = true;

                if(value != null){
                    if(value.getClass() == Date.class) {
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        value = ((Date)param).toLocalDate().format(f);
                        isChangedString = true;
                    }
                    if(value.getClass() == Time.class) {
                        value = ((Time)param).toString();
                        isChangedString = true;
                    }
                    if(value.getClass() == Timestamp.class) {
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        value = ((Timestamp)param).toLocalDateTime().format(f);
                        isChangedString = true;
                    }
                    if(value.getClass() == LocalDateTime.class) {
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        value = ((LocalDateTime)param).format(f);
                        isChangedString = true;
                    }
                }

                if(value != null && isChangedString){
                    statement.setString(i, value.toString());
                }
                else{
                    statement.setObject(i, value);
                }

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
                    Object value = result.getObject(i);

                    if(value != null && value.getClass() == String.class) {
                        // タイムスタンプを取得
                        Object convertResult =  getTimestamp((String)value);

                        // 取得できない場合は日付を取得
                        if(convertResult == null) {
                            convertResult = getDate((String)value);
                        }

                        // 取得できない場合は時刻を取得
                        if(convertResult == null) {
                            convertResult = getTime((String)value);
                        }

                        // 日付系が取得できれば設定値に設定する
                        if(convertResult != null) {
                            value = convertResult;
                        }
                    }

                    recodeMap.put(metaData.getColumnName(i), value);
                }
                resultList.add(recodeMap);
            }

        } catch (SQLException e) {
            throw new Exception(e);
        }
        return resultList;
    }

    /**
     * 文字列をタイムスタンプに変換
     * @param value 文字列
     * @return タイムスタンプ(変換できない場合はnull)
     */
    private Timestamp getTimestamp(String value) {
        try{
            return Timestamp.valueOf(value.replace('T', ' ').replace('/', '-'));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 文字列を日付に変換
     * @param value 文字列
     * @return 日付(変換できない場合はnull)
     */
    private Date getDate(String value) {
        try{
            return Date.valueOf(value.replace('/', '-'));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 文字列を時刻に変換
     * @param value 文字列
     * @return 時刻(変換できない場合はnull)
     */
    private Time getTime(String value) {
        try{
            return Time.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
