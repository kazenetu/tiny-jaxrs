package web.common.util;

public class MessageEntity {
    private String key;
    private String value;

    public MessageEntity(){
        this.key = "";
        this.value = "";
    }

    public MessageEntity(String key,String value){
        this.key = key;
        this.value =value;
    }

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }
    /**
     * @param key セットする key
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @return value
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value セットする value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
