package web.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
         * メッセージをリソースファイルから取得
         * @return メッセージ一覧
         * @throws Exception
         */
        public Map<String,String> getJson() throws Exception{
            // JSONファイルはmessage.jsonから取得
            String filePath = this.getClass().getClassLoader().getResource("message.json").getPath();
            StringBuilder builder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String string = reader.readLine();
                while (string != null){
                    builder.append(string + System.getProperty("line.separator"));
                    string = reader.readLine();
                }
            }
            String json = builder.toString();

            ObjectMapper mapper = new ObjectMapper();

            // json文字列をPasswordChangeにデシリアライズする
            //List<MessageEntity> result = Arrays.asList(mapper.readValue(json,  MessageEntity[].class));
            List<MessageEntity> result = mapper.readValue(json, new TypeReference<List<MessageEntity>>() {
            });

            Map<String,String> entities = new HashMap<>();
            result.forEach(row->{
                entities.put(row.getKey(), row.getValue());
            });

            return entities;
        }
    }

    /**
     * コンストラクタ
     */
    private MessageUtil(){
        // メッセージを取得する
        try (MessageModel model = new MessageModel()) {
            // jsonファイルから取得
            messages = model.getJson();
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
