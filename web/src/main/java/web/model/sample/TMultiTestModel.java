package web.model.sample;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.common.base.Model;
import web.entity.sample.TMultiTestEntity;
import web.entity.sample.TMultiTestHistoryEntity;

public class TMultiTestModel extends Model {
    private static Logger logger = LoggerFactory.getLogger(TMultiTestModel.class);

    public boolean insert(TMultiTestEntity entity){
        String sql = "INSERT INTO t_multi_test(id, name) VALUES (?, ?);";

        ArrayList<Object> params = new ArrayList<>();
        params.add(entity.getId());
        params.add(entity.getName());

        try(TMultiTestHistoryModel model = new TMultiTestHistoryModel(this)) {
            db.setTransaction();

            // メインテーブル：複数モデル登録テストを登録
            if (db.execute(sql, params) <= 0) {
                db.rollback();
                return false;
            }

            //履歴を登録
            TMultiTestHistoryEntity historyEntity = new TMultiTestHistoryEntity();
            historyEntity.setId(entity.getId());
            if(!model.insert(historyEntity)){
                db.rollback();
                return false;
            }

            db.commit();

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;

    }
}
