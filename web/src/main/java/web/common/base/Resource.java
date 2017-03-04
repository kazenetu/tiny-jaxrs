package web.common.base;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

/**
 * リソースクラスのスーパークラス
 */
public abstract class Resource {

    @Inject
    protected HttpSession session;

    /**
     * セッションの破棄と生成
     * @param servletRequest 生成の際に利用するHttpServletRequest
     */
    protected void refreshSessionId(final HttpServletRequest servletRequest) {
        //セッションの破棄
        session.invalidate();

        //セッション作成
        session = servletRequest.getSession(true);
    }

    /**
     * 認証確認
     * @param userId ログインしているユーザー
     */
    protected void authCheck(String userId) {
        if (session.getAttribute("userId") == null || !((String) session.getAttribute("userId")).equals(userId)) {
            //認証エラー時は401例外を出す
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
    }

}
