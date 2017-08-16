package web.resource.sample;

import java.io.BufferedInputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.process.internal.RequestScoped;

import web.common.base.Resource;

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
            StringBuilder fileData = new StringBuilder();

            List<BodyPart> bodyPartList = multiPart.getBodyParts();

            for (BodyPart bodyPart : bodyPartList) {
                if(bodyPart.getContentDisposition().getFileName() != null){
                    BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPart.getEntity();
                    BufferedInputStream bf = new BufferedInputStream(bodyPartEntity.getInputStream());
                    byte[] fbytes = new byte[1024];

                    while ((bf.read(fbytes)) >= 0) {
                        fileData.append(fbytes);
                    }
                }
            }

            // 取得したデータをBase64に変換して返す
            return Response.ok(Base64.encodeAsString(fileData.toString())).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

}
