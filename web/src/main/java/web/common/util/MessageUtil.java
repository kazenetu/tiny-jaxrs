package web.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web.common.base.Model;

/**
 * メッセージ取得ユーティリティ
 * シングルトンで実装
 */
public class MessageUtil {
    /**
     * ユーティリティクラス本体
     */
    private static MessageUtil instance = new MessageUtil();

    /***
     * メッセージ情報
     */
    private Map<String,String> messages = null;

    /**
     * DBからメッセージ情報を取得するModelクラス
     */
    private class MessageModel extends Model {

        /**
         * メッセージをDBから取得する
         * @return メッセージ一覧
         * @throws Exception
         */
        public Map<String,String> selectAll() throws Exception{
            String sql = "select message_id,message from MT_MSG;";

            Map<String,String> entities = new HashMap<>();

            try {
                List<Map<String,Object>> result = db.query(sql, new ArrayList<>());
                if (!result.isEmpty()) {
                    result.forEach(row->{
                        entities.put((String)getColumnValue(row,"message_id"), (String)getColumnValue(row,"message"));
                    });
                }
            } catch (Exception e) {
                throw new Exception(e);
            }

            return entities;
        }
    }

    /**
     * コンストラクタ
     */
    private MessageUtil(){
        // メッセージを取得する
        try (MessageModel model = new MessageModel()) {
               messages = model.selectAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * インスタンス取得
     */
    public static MessageUtil getInstance(){
        return instance;
    }

    /**
     * メッセージ取得
     * @param key メッセージID
     * @return メッセージ本文
     */
    public String getMessage(String key){
        String value = "";
        if(messages != null){
            value = messages.get(key);
        }
        if(value == null){
            value = "";
        }
        return value;
    }
}
