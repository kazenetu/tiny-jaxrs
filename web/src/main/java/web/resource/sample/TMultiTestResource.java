package web.resource.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.internal.RequestScoped;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import web.common.base.RequestEntity;
import web.common.base.Resource;
import web.common.base.ResposeEntity;
import web.entity.sample.TMultiTestEntity;
import web.model.sample.TMultiTestModel;
import web.resource.MessagesConst;

@RequestScoped
@Path("multi")
public class TMultiTestResource extends Resource {

    /**
     * 登録テスト
     * @param json
     * @return
     */
    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String json){
        ObjectMapper mapper = new ObjectMapper();

        try (TMultiTestModel model = new TMultiTestModel()) {

            // json文字列をPasswordChangeにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, TMultiTestEntity.class);
            RequestEntity<TMultiTestEntity> instance = mapper.readValue(json, type);

            ResposeEntity<String> result = null;
            if (model.insert(instance.getRequestData())) {
                result = new ResposeEntity<String>(ResposeEntity.Result.OK,"","");
            } else {
                result = new ResposeEntity<String>(ResposeEntity.Result.NG,getMessage(MessagesConst.ErrorCodes.INSERT),"");
            }

            // 結果を返す
            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
