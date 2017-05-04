package web.entity.sample;
/**
 * 複数モデル登録テスト
 */
public class TMultiTestEntity {

    /**
     * ID
     */
    private String id;

    /**
     * 名前
     */
    private String name;

    /**
     * IDを設定
     * @param value 設定値
     */
    public void setId(String value) {
        id = value;
    }

    /**
     * IDを取得
     * @return idを返す
     */
    public String getId(){
        return id;
    }

    /**
     * 名前を設定
     * @param value 設定値
     */
    public void setName(String value) {
        name = value;
    }

    /**
     * 名前を取得
     * @return nameを返す
     */
    public String getName(){
        return name;
    }
}
