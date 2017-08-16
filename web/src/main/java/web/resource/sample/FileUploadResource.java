package web.resource.sample;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.process.internal.RequestScoped;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import web.common.base.RequestEntity;
import web.common.base.Resource;
import web.common.base.ResponseEntity;
import web.entity.sample.UploadFileEntity;

@RequestScoped
@Path("fileupload")
public class FileUploadResource extends Resource {
    /**
     * ファイルからBase64を生成する
     * @param multiPart ファイル情報
     */
    @POST
    @Path("convert/base64")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response test(FormDataMultiPart multiPart) {
        try {
            List<ByteBuffer> bytes = new ArrayList<>();
            int byteCount = 0;

            List<BodyPart> bodyPartList = multiPart.getBodyParts();

            for (BodyPart bodyPart : bodyPartList) {
                // fileだけ処理
                if(bodyPart.getContentDisposition().getFileName() != null){

                    // ファイルバイナリ取得
                    BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPart.getEntity();
                    BufferedInputStream bf = new BufferedInputStream(bodyPartEntity.getInputStream());
                    byte[] fbytes = new byte[1024];

                    while ((bf.read(fbytes)) >= 0) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(fbytes.length);
                        byteBuffer.put(fbytes);
                        bytes.add(byteBuffer);

                        // バイト数を加算
                        byteCount += fbytes.length;
                    }
                }
            }

            // ファイルバイナリを結合する
            ByteBuffer targetData = ByteBuffer.allocate(byteCount);
            for(ByteBuffer byteBuffer:bytes){
                targetData.put(byteBuffer.array());
            }

            // 取得したデータをBase64に変換して返す
            String result = Base64.getEncoder().encodeToString(targetData.array());
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    /**
     * アップロード（ファイル作成）
     * @param json リクエスト情報
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {

            // json文字列をPasswordChangeにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UploadFileEntity.class);
            RequestEntity<UploadFileEntity> instance = mapper.readValue(json, type);

            UploadFileEntity entity = instance.getRequestData();

            String filePath = this.getClass().getClassLoader().getResource("test.db").getPath();
            filePath = filePath.substring(0,filePath.indexOf("test.db")) + entity.getFileName();

            // ファイル作成
            String data = entity.getFileData();
            FileOutputStream output=new FileOutputStream(filePath);
            output.write(Base64.getDecoder().decode(data));
            output.flush();
            output.close();

            return Response.ok(mapper.writeValueAsString(new ResponseEntity<String>(ResponseEntity.Result.OK,"",filePath+"に作成しました"))).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
