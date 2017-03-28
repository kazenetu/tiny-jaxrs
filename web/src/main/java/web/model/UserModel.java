package web.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.common.base.Model;
import web.entity.UserEntity;
import web.entity.UserListEntity;

/**
 * ユーザーモデル
 */
public class UserModel extends Model{
    private static Logger logger = LoggerFactory.getLogger(UserModel.class);

    /**
     * ログインチェック
     * @param userID ユーザーID
     * @param password パスワード
     * @return ユーザー情報(検索できない場合はnull)
     * @throws Exception
     */
    public Optional<UserEntity> login(String userID, String password) throws Exception {
        String sql = "select NAME from MT_USER where USER_ID=?;";

        ArrayList<Object> params = new ArrayList<>();
        params.add(userID);

        UserEntity entity = null;
        try {
            List<Map<String,Object>> result = db.query(sql, params);
            if (!result.isEmpty()) {
                Map<String,Object> row = result.get(0);

                entity = new UserEntity();
                entity.setName(userID);
                entity.setName((String)getColumnValue(row,"NAME"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        return Optional.ofNullable(entity);
    }

    /**
     * ユーザー全レコードを取得する
     * @return ユーザー全レコードのリスト
     * @throws Exception
     */
    public List<UserEntity> getUsers() throws Exception{
        String sql = "select USER_ID,NAME,PASSWORD from MT_USER;";

        List<UserEntity> entities = new ArrayList<>();

        try {
            List<Map<String,Object>> result = db.query(sql, new ArrayList<>());
            if (!result.isEmpty()) {
                result.forEach(row->{
                    entities.add(new UserEntity(
                            (String)getColumnValue(row,"USER_ID"),
                            (String)getColumnValue(row,"NAME"),
                            (String)getColumnValue(row,"PASSWORD"),0));
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        return entities;
    }

    /**
     * 検索結果のページ総数を取得する
     * @return ページ総数(検索件数0件の場合は-1)
     */
    public int getUserPageCount(UserListEntity seachCondition){
        String sql = "select cast(count(USER_ID) as int) CNT from MT_USER ";

        // 検索条件
        String searchUserId = seachCondition.getSearchUserId();

        int recordCount = 0;

        try {
            ArrayList<Object> params = new ArrayList<>();

            if(!isNullorEmpty(searchUserId)){
                sql += "where USER_ID like ? ";
                params.add("%" + searchUserId + "%");
            }

            List<Map<String,Object>> result = db.query(sql, params);

            Map<String,Object> row = result.get(0);
            recordCount = (int)getColumnValue(row,"CNT");
        } catch (Exception e) {
            logger.error(e.getMessage());
            //throw new Exception(e);
        }

        // トータルページ数を返す
        return getTotalPageCount(recordCount);
    }

    /**
     * ユーザーのページ分を取得する
     * @return ユーザーのリスト
     * @throws Exception
     */
    public List<UserEntity> getUsers(UserListEntity seachCondition) throws Exception{
        String sql = "select * from MT_USER ";

        // 検索条件
        int pageIndex = seachCondition.getPageIndex();
        String searchUserId = seachCondition.getSearchUserId();

        // パラメータの設定
        ArrayList<Object> params = new ArrayList<>();
        if(!isNullorEmpty(searchUserId)){
            sql += "where USER_ID like ? ";
            params.add("%" + searchUserId + "%");
        }
        sql += " ORDER BY USER_ID LIMIT ? OFFSET ?";
        params.add(PAGE_COUNT);
        params.add(pageIndex*PAGE_COUNT);

        List<UserEntity> entities = new ArrayList<>();

        try {
            List<Map<String,Object>> result = db.query(sql, params);
            if (!result.isEmpty()) {
                result.forEach(row->{
                    UserEntity entity = new UserEntity(
                            (String)getColumnValue(row,"USER_ID"),
                            (String)getColumnValue(row,"NAME"),
                            (String)getColumnValue(row,"PASSWORD"),0);

                    entity.setDate((Date)getColumnValue(row,"date_data"));
                    entity.setTime((Time)getColumnValue(row,"time_data"));
                    entity.setTs((Timestamp)getColumnValue(row,"ts_data"));

                    entities.add(entity);
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        return entities;
    }

    /**
     * ユーザーを取得する
     * @param userID ユーザーID
     * @return ユーザー情報(検索できない場合はnull)
     * @throws Exception
     */
    public Optional<UserEntity> getUser(String userID) throws Exception{
        String sql = "select * from MT_USER where USER_ID like ?;";

        // パラメータの設定
        ArrayList<Object> params = new ArrayList<>();
        params.add(userID);

        UserEntity entity = null;

        try {
            List<Map<String,Object>> result = db.query(sql, params);
            if (!result.isEmpty()) {
                Map<String,Object> row = result.get(0);

                entity = new UserEntity();
                entity.setId((String)getColumnValue(row,"USER_ID"));
                entity.setName((String)getColumnValue(row,"NAME"));
                entity.setPassword((String)getColumnValue(row,"PASSWORD"));
                entity.setDate((Date)getColumnValue(row,"date_data"));
                entity.setTime((Time)getColumnValue(row,"time_data"));
                entity.setTs((Timestamp)getColumnValue(row,"ts_data"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        return Optional.ofNullable(entity);
    }

    /**
     * ユーザーの登録
     * @param userData ユーザーデータ
     * @return 成否
     */
    public boolean insert(UserEntity userData){
        String sql = "insert into mt_user(user_id, name, password, date_data, time_data, ts_data) values (?, ?, ?, ?, ?, ?);";

        ArrayList<Object> params = new ArrayList<>();
        params.add(userData.getId());
        params.add(userData.getName());
        params.add(userData.getPassword());
        params.add(userData.getDate());
        params.add(userData.getTime());
        params.add(userData.getTs());

        try {
            db.setTransaction();

            if (db.execute(sql, params) > 0) {
                db.commit();
                return true;
            } else {
                db.rollback();
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    /**
     * ユーザーの更新
     * @param userData ユーザーデータ
     * @return 成否
     */
    public boolean update(UserEntity userData){
        String sql = "update MT_USER set Name = ?,PASSWORD=?,date_data=?,time_data=?,ts_data=? where USER_ID=?;";

        ArrayList<Object> params = new ArrayList<>();
        params.add(userData.getName());
        params.add(userData.getPassword());
        params.add(userData.getDate());
        params.add(userData.getTime());
        params.add(userData.getTs());
        params.add(userData.getId());

        try {
            db.setTransaction();

            if (db.execute(sql, params) > 0) {
                db.commit();
                return true;
            } else {
                db.rollback();
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    /**
     * ユーザーの削除
     * @param userData ユーザーデータ
     * @return 成否
     */
    public boolean delete(UserEntity userData){
        String sql = "delete from MT_USER where USER_ID=?;";

        ArrayList<Object> params = new ArrayList<>();
        params.add(userData.getId());

        try {
            db.setTransaction();

            if (db.execute(sql, params) > 0) {
                db.commit();
                return true;
            } else {
                db.rollback();
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    /**
     * パスワード変更
     * @param userID ユーザーID
     * @param password パスワード
     * @param newPassword 新パスワード
     * @return 正常終了結果
     */
    public boolean passwordChange(String userID, String password, String newPassword) {
        String sql = "update MT_USER set PASSWORD=? where USER_ID=? and PASSWORD=?;";

        ArrayList<Object> params = new ArrayList<>();
        params.add(newPassword);
        params.add(userID);
        params.add(password);

        try {
            db.setTransaction();

            if (db.execute(sql, params) > 0) {
                db.commit();
                return true;
            } else {
                db.rollback();
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;
    }
}
