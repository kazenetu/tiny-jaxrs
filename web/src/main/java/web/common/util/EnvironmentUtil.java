package web.common.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 環境変数取得クラス
 */
public class EnvironmentUtil {
    private static Logger logger = LoggerFactory.getLogger(EnvironmentUtil.class);

    /**
     * シングルトンインスタンス
     */
    private static EnvironmentUtil instance = new EnvironmentUtil();

    /**
     * SQLログを出力するか否か
     */
    private boolean outputSqlLog = false;

    /**
     * privateなコンストラクタ
     */
    private EnvironmentUtil() {
        InitialContext context;
        try {
            context = new InitialContext();

            if("true".equals(context.lookup("java:comp/env/OUTPUT_SQL_LOG"))) {
                outputSqlLog = true;
            }
        } catch (NamingException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * インスタンス取得
     * @return シングルトンインスタンス
     */
    public static EnvironmentUtil getInstance() {
        return instance;
    }

    /**
     * SQLログを出力するか否か
     * @return SQLログを出力する場合はtrue
     */
    public boolean isOutputSqlLog() {
        return outputSqlLog;
    }

}
