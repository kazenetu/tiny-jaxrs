package web.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.internal.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.common.base.Resource;
import web.entity.UserData;
import web.model.UserModel;

@RequestScoped
@Path("user")
public class UserResource extends Resource{
    private static Logger logger = LoggerFactory.getLogger(UserResource.class);

    /**
     * ログイン
     * @param servletRequest リクエストオブジェクト
     * @param userId ユーザーID
     * @param password パスワード
     * @return レスポンス
     */
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context final HttpServletRequest servletRequest,@QueryParam("userId") String userId,@QueryParam("password") String password) {

        //すでにログイン済みの場合はSessionIDの破棄と生成を行う
        if(session.getAttribute("userId") != null){
            refreshSessionId(servletRequest);
        }

        try(UserModel userModel=new UserModel()){
            Optional<UserData> userData = userModel.login(userId, password);

            String result =  userData.map(data->{
                //SessionIDの破棄と生成を行う
                refreshSessionId(servletRequest);

                // セッションにログインIDを設定
                session.setAttribute("userId", userId);
                return String.format("{\"result\":\"OK\",\"name\":\"%s\"}", data.getName());
            }).orElse("{\"result\":\"NG\"}");

            return Response.ok(result) .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ユーザー一覧取得
     * @param servletRequest リクエストオブジェクト
     * @param userId ユーザーID
     * @return レスポンス
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userlist(@QueryParam("userId") String userId) {
        //認証チェック（認証エラー時は401例外を出す）
        authCheck(userId);

        List<UserData> users = new ArrayList<>();
        try(UserModel userModel=new UserModel()){
            users =  userModel.getUsers();

            ObjectMapper mapper = new ObjectMapper();
            return Response.ok(mapper.writeValueAsString(users))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }


}
