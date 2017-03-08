package web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.common.base.Model;
import web.entity.UserData;

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
    public Optional<UserData> login(String userID, String password) throws Exception {
        String sql = "select NAME from MT_USER where USER_ID=?;";

        ArrayList<Object> params = new ArrayList<>();
        params.add(userID);

        UserData userData = null;
        try {
            List<Map<String,Object>> result = db.query(sql, params);
            if (!result.isEmpty()) {
                Map<String,Object> row = result.get(0);

                userData = new UserData();
                userData.setName(userID);
                userData.setName((String)row.get("NAME"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        return Optional.ofNullable(userData);
    }

    /**
     * ユーザー全レコードを取得する
     * @return ユーザー全レコードのリスト
     * @throws Exception
     */
    public List<UserData> getUsers() throws Exception{
        String sql = "select USER_ID,NAME,PASSWORD from MT_USER;";

        List<UserData> users = new ArrayList<>();

        try {
            List<Map<String,Object>> result = db.query(sql, new ArrayList<>());
            if (!result.isEmpty()) {
                result.forEach(row->{
                    users.add(new UserData(row.get("USER_ID").toString(),row.get("NAME").toString(),row.get("PASSWORD").toString(),0));
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        return users;
    }

    private final int PAGE_COUNT = 10;
    /**
     * 検索結果のページ総数を取得する
     * @return ページ総数
     */
    public int getUserPageCount(String searchUserId){
        String sql = "select cast(count(USER_ID) as int) CNT from MT_USER ";

        int recordCount = 0;

        try {
            ArrayList<Object> params = new ArrayList<>();
            if(!(searchUserId == null || "".equals(searchUserId))){
                sql += "where USER_ID like ? ";
                params.add("%" + searchUserId + "%");
            }

            List<Map<String,Object>> result = db.query(sql, params);

            Map<String,Object> row = result.get(0);
            recordCount = (int)row.get("CNT");
        } catch (Exception e) {
            logger.error(e.getMessage());
            //throw new Exception(e);
        }

        int pageCount = recordCount / PAGE_COUNT;
        if(recordCount <= PAGE_COUNT){
            pageCount = 0;
        }else{
            if(recordCount - pageCount*PAGE_COUNT > 0){
                pageCount++;
            }
        }

        return pageCount;
    }

    /**
     * ユーザーのページ分を取得する
     * @return ユーザーのリスト
     * @throws Exception
     */
    public List<UserData> getUsers(int pageIndex,String searchUserId) throws Exception{
        String sql = "select USER_ID,NAME,PASSWORD from MT_USER ";

        ArrayList<Object> params = new ArrayList<>();
        if(!(searchUserId == null || "".equals(searchUserId))){
            sql += "where USER_ID like ? ";
            params.add("%" + searchUserId + "%");
        }
        sql += " ORDER BY USER_ID LIMIT ? OFFSET ?";
        params.add(PAGE_COUNT);
        params.add(pageIndex*PAGE_COUNT);

        List<UserData> users = new ArrayList<>();

        try {
            List<Map<String,Object>> result = db.query(sql, params);
            if (!result.isEmpty()) {
                result.forEach(row->{
                    users.add(new UserData(row.get("USER_ID").toString(),row.get("NAME").toString(),row.get("PASSWORD").toString(),0));
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        return users;
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
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        return false;
    }
}
