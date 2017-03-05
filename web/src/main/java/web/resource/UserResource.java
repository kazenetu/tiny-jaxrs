package web.resource;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import web.common.base.Resource;
import web.common.util.PdfUtil;
import web.entity.PasswordChange;
import web.entity.TestData;
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

    @POST
    @Path("passwordChange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response passwordChange(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try(UserModel userModel=new UserModel()){

            // json文字列をPasswordChangeにデシリアライズする
            PasswordChange instance = mapper.readValue(json, PasswordChange.class);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getId());


            String result = "";

            // パスワード変更SQLを発行
            if ( userModel.passwordChange(instance.getId(), instance.getPassword(),instance.getNewPassword())) {
                result = "{\"result\":\"OK\"}";
            }else{
                result = "{\"result\":\"NG\"}";
            }

            // パスワード変更結果を返す
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

    @POST
    @Path("download")
    @Produces(MediaType.TEXT_PLAIN)
    public Response download(@FormParam("userId") String userId,@FormParam("userName") String userName) {
        //認証チェック（認証エラー時は401例外を出す）
        authCheck(userId);

        List<TestData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new TestData("Name" + i, 20 + i));
        }

        String fileName = "テスト_" + userName + ".csv";
        try {
            return Response.ok(list)
                    .header("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    @POST
    @Path("downloadPDF")
    @Produces("application/pdf")
    public Response downloadPDF(@FormParam("userId") String userId,@FormParam("userName") String userName) {
        //認証チェック（認証エラー時は401例外を出す）
        authCheck(userId);

        List<UserData> users = new ArrayList<>();
        try(UserModel userModel=new UserModel();PdfUtil pdfUtil = new PdfUtil();){
            // DBからデータ取得
            users =  userModel.getUsers();

            // PDFのデータソースとして設定
            JRDataSource dataSource = new JRBeanCollectionDataSource(users);

            Map<String, Object> params = new HashMap<>();

            // PDFの動的作成と取得
            byte[] result = pdfUtil.getPdfBytes("UserList", params, dataSource);

            // サンプルのファイル名
            String fileName = "テスト_" + userName + ".pdf";

            // 結果を返す
            return Response.ok(result)
                     .header("Content-Disposition", "attachment; filename=" +
                     URLEncoder.encode(fileName, "utf-8"))
                    .build();

        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

}
