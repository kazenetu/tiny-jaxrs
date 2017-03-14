package web.common.base;

/**
 * リクエスト用エンティティクラスのスーパークラス
 */
public class RequestEntity<T> {
    /**
     * ログインしているユーザーのID
     */
    private String loginUserId;

    /**
     * ジェネリクス元を取得
     */
    private T requestData;

    /**
     * @return loginUserId
     */
    public String getLoginUserId() {
        return loginUserId;
    }

    /**
     * @param loginUserId セットする loginUserId
     */
    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    /**
     * @return requestData
     */
    public T getRequestData() {
        return requestData;
    }

    /**
     * @param requestData セットする requestData
     */
    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }

}
