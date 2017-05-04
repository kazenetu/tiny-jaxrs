package web.model.sample;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.common.base.Model;
import web.entity.sample.TMultiTestHistoryEntity;

public class TMultiTestHistoryModel extends Model {
    private static Logger logger = LoggerFactory.getLogger(TMultiTestHistoryModel.class);

    /**
     * コンストラクタ：単独で利用する場合
     */
    public TMultiTestHistoryModel(){
        super();
    }

    /**
     * コンストラクタ：別のModelクラスから呼ばれる場合
     * @param model コネクションが生成されているModelクラスのインスタンス
     */
    public TMultiTestHistoryModel(Model model){
        super(model);
    }

    /**
     * 履歴の登録
     * @param entity 履歴エンティティ
     * @return 登録可否
     */
    public boolean insert(TMultiTestHistoryEntity entity){
        StringBuilder sql = new StringBuilder(100);
        sql.append("INSERT INTO t_multi_test_history(id, history_no, name) ");
        sql.append("SELECT");
        sql.append("  t_multi_test.id");
        sql.append("  , coalesce(max(history_no), 0) + 1 history_no");
        sql.append("  , t_multi_test.name ");
        sql.append("from");
        sql.append("  t_multi_test ");
        sql.append("  left join t_multi_test_history ");
        sql.append("    on t_multi_test.id = t_multi_test_history.id ");
        sql.append("group by");
        sql.append("  t_multi_test.id");
        sql.append("  , t_multi_test.name ");
        sql.append("having");
        sql.append("  t_multi_test.id = ? ");

        ArrayList<Object> params = new ArrayList<>();
        params.add(entity.getId());

        try {
            db.setTransaction();

            if (db.execute(sql.toString(), params) > 0) {
                db.commit();
                return true;
            } else {
                db.rollback();
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage()+":"+sql.toString());
        }

        return false;

    }
}
