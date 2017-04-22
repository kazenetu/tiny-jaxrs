package web.model.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.common.base.Model;
import web.entity.sample.UploadEntity;

public class UploadModel extends Model {
    private static Logger logger = LoggerFactory.getLogger(UploadModel.class);

    /**
     * ファイルの登録
     * @param uploadEntity ファイルエンティティ
     * @return 成否
     */
    public boolean insert(UploadEntity uploadEntity){
        String sql = "INSERT INTO T_FILE(ID, IMAGE_DATA, FILE_NAME, TAG) SELECT COALESCE(MAX(ID)+1, 1) ,?,?,? FROM T_FILE;";

        ArrayList<Object> params = new ArrayList<>();
        params.add(uploadEntity.getImageData());
        params.add(uploadEntity.getFileName());
        params.add(uploadEntity.getTag());

        try {
            db.setTransaction();

            if (db.execute(sql, params) > 0) {
                db.commit();
                return true;
            } else {
                db.rollback();
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    /**
     * 全レコードを取得する
     * @return 全レコードのリスト
     * @throws Exception
     */
    public List<UploadEntity> getDataList() throws Exception{
        String sql = "SELECT IMAGE_DATA, FILE_NAME, TAG FROM T_FILE ORDER BY ID;";

        List<UploadEntity> entities = new ArrayList<>();

        try {
            List<Map<String,Object>> result = db.query(sql, new ArrayList<>());
            if (!result.isEmpty()) {
                result.forEach(row->{

                    UploadEntity entity = new UploadEntity();
                    entity.setImageData((String)(getColumnValue(row,"IMAGE_DATA")));
                    entity.setFileName((String)(getColumnValue(row,"FILE_NAME")));
                    entity.setTag((String)(getColumnValue(row,"TAG")));

                    entities.add(entity);
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        return entities;
    }
}
