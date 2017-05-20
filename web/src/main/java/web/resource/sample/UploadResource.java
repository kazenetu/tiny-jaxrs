package web.resource.sample;

import java.io.FileOutputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.process.internal.RequestScoped;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import web.common.base.RequestEntity;
import web.common.base.Resource;
import web.common.base.ResponseEntity;
import web.entity.sample.UploadEntity;
import web.model.sample.UploadModel;
import web.resource.MessagesConst;

@RequestScoped
@Path("upload")
public class UploadResource extends Resource {
    /**
     * アップロード（ファイル作成）
     * @param json リクエスト情報
     */
    @POST
    @Path("test")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {

            // json文字列をPasswordChangeにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UploadEntity.class);
            RequestEntity<UploadEntity> instance = mapper.readValue(json, type);

            UploadEntity entity = instance.getRequestData();

            String filePath = this.getClass().getClassLoader().getResource("test.db").getPath();
            filePath = filePath.substring(0,filePath.indexOf("test.db")) + entity.getFileName();


            //byte[] imageBinary = java.util.Base64.getDecoder().decode(entity.getImageData());
            String data = entity.getImageData();
            data = data.substring(data.indexOf("base64,")+7);

            byte[] imageBinary = Base64.decode(data.getBytes());

            FileOutputStream output=new FileOutputStream(filePath);
            output.write(imageBinary);
            output.flush();
            output.close();


            return Response.ok(mapper.writeValueAsString(new ResponseEntity<String>(ResponseEntity.Result.OK,"",filePath+"に作成しました"))).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    /**
     * アップロード（DB登録）
     * @param json リクエスト情報
     */
    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UploadModel model = new UploadModel()) {

            // json文字列をPasswordChangeにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UploadEntity.class);
            RequestEntity<UploadEntity> instance = mapper.readValue(json, type);

            ResponseEntity<String> result = null;
            if (model.insert(instance.getRequestData())) {
                result = new ResponseEntity<String>(ResponseEntity.Result.OK,"","");
            } else {
                result = new ResponseEntity<String>(ResponseEntity.Result.NG,getMessage(MessagesConst.ErrorCodes.INSERT),"");
            }

            // 結果を返す
            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    /**
     * 全レコードを取得
     */
    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UploadModel model = new UploadModel()) {

            /*
            // json文字列をPasswordChangeにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UploadEntity.class);
            RequestEntity<UploadEntity> instance = mapper.readValue(json, type);
            */


            // 検索条件での検索結果を取得する
            List<UploadEntity> entities = model.getDataList();

            // 結果を返す
            ResponseEntity<List<UploadEntity>> result = null;
            result = new ResponseEntity<List<UploadEntity>>(ResponseEntity.Result.OK,"",entities);

            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

}
