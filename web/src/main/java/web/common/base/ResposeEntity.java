package web.common.base;

/**
 * レスポンス用エンティティクラスのジェネリクスクラス
 */
public class ResposeEntity<T> {

    /**
     * 結果
     */
    public static enum Result{
        OK,NG
    }

    /**
     * ステータス
     */
    private Result result;

    /**
     * エラーメッセージ
     */
    private String errorMessage;

    /**
     * ジェネリクス元を取得
     */
    private T responseData;

    public ResposeEntity(Result result,String errorMessage){
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public ResposeEntity(Result result,String errorMessage,T responseData){
        this.result = result;
        this.errorMessage = errorMessage;
        this.responseData = responseData;
    }

    /**
     * @return result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result セットする result
     */
    public void setResult(Result status) {
        this.result = status;
    }

    /**
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage セットする errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return responseData
     */
    public T getResponseData() {
        return responseData;
    }

    /**
     * @param responseData セットする responseData
     */
    public void setResponseData(T responsetData) {
        this.responseData = responsetData;
    }
}
