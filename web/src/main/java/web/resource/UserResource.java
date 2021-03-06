package web.resource;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.process.internal.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import web.common.base.CsvEntity;
import web.common.base.RequestEntity;
import web.common.base.Resource;
import web.common.base.ResponseEntity;
import web.common.base.StandardCsvFormatter;
import web.common.util.ExcludedExceptionUtil;
import web.common.util.PdfUtil;
import web.entity.PasswordChangeEntity;
import web.entity.UserEntity;
import web.entity.UserListEntity;
import web.model.UserModel;

@RequestScoped
@Path("user")
public class UserResource extends Resource {
    private static Logger logger = LoggerFactory.getLogger(UserResource.class);

    /**
     * ログイン
     * @param servletRequest リクエストオブジェクト
     * @param json リクエスト情報
     */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context final HttpServletRequest servletRequest, String json) {

        //すでにログイン済みの場合はSessionIDの破棄と生成を行う
        if (session.getAttribute("userId") != null) {
            refreshSessionId(servletRequest);
        }

        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {
            // json文字列をPasswordChangeにデシリアライズする
            UserEntity instance = mapper.readValue(json, UserEntity.class);

            Optional<UserEntity> entity = model.login(instance.getId(), instance.getPassword());

            ResponseEntity<UserEntity> result = entity.map(data -> {
                //SessionIDの破棄と生成を行う
                refreshSessionId(servletRequest);

                // セッションにログインIDを設定
                session.setAttribute("userId", instance.getId());

                // セッションにログイン名を設定
                session.setAttribute("userName", data.getName());

                // 結果を返す
                return new ResponseEntity<UserEntity>(ResponseEntity.Result.OK,"",entity.get());
            }).orElse(new ResponseEntity<UserEntity>(ResponseEntity.Result.NG,getMessage(MessagesConst.ErrorCodes.LOGIN),null));

            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ログアウト
     * @param servletRequest リクエストオブジェクト
     * @return レスポンス
     */
    @POST
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context final HttpServletRequest servletRequest) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            //セッションの破棄
            session.invalidate();

            ResponseEntity<String> result = new ResponseEntity<String>(ResponseEntity.Result.OK,"","");

            // 結果を返す
            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ログインユーザーのパスワード変更
     * @param json リクエスト情報
     * @return 変更成否
     */
    @POST
    @Path("passwordChange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response passwordChange(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {

            // json文字列をPasswordChangeにデシリアライズする
            PasswordChangeEntity instance = mapper.readValue(json, PasswordChangeEntity.class);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getId());

            ResponseEntity<String> result = null;

            // パスワード変更SQLを発行
            if (model.passwordChange(instance.getId(), instance.getPassword(), instance.getNewPassword())) {
                result = new ResponseEntity<String>(ResponseEntity.Result.OK,"","");
            } else {
                result = new ResponseEntity<String>(ResponseEntity.Result.NG,getMessage(MessagesConst.ErrorCodes.PASSWORD_CHANGE),"");
            }

            // 結果を返す
            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ユーザー情報の登録
     * @param json ログインユーザーIDと登録情報
     * @return 変更成否
     */
    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {

            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserEntity.class);
            RequestEntity<UserEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            ResponseEntity<String> result = null;

            // 登録SQLを発行
            if (model.insert(instance.getRequestData())) {
                result = new ResponseEntity<String>(ResponseEntity.Result.OK,"","");
            } else {
                result = new ResponseEntity<String>(ResponseEntity.Result.NG,getMessage(MessagesConst.ErrorCodes.INSERT),"");
            }

            // 結果を返す
            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ユーザー情報の更新
     * @param json ログインユーザーIDと変更情報
     * @return 変更成否
     */
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {

            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserEntity.class);
            RequestEntity<UserEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            ResponseEntity<String> result = null;

            // 更新SQLを発行
            if (model.update(instance.getRequestData())) {
                result = new ResponseEntity<String>(ResponseEntity.Result.OK,"","");
            } else {
                result = new ResponseEntity<String>(ResponseEntity.Result.NG,getMessage(MessagesConst.ErrorCodes.UPDATE),"");
            }

            // 結果を返す
            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ユーザー情報の削除
     * @param json ログインユーザーIDと削除情報
     * @return 削除成否
     */
    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {

            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserEntity.class);
            RequestEntity<UserEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            ResponseEntity<String> result = null;

            // 削除SQLを発行
            if (model.delete(instance.getRequestData())) {
                result = new ResponseEntity<String>(ResponseEntity.Result.OK,"","");
            } else {
                result = new ResponseEntity<String>(ResponseEntity.Result.NG,getMessage(MessagesConst.ErrorCodes.DELETE),"");
            }

            // 結果を返す
            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * 検索結果のページ総数を取得する
     * @param json ログインユーザーIDと検索条件
     * @return レスポンス
     */
    @POST
    @Path("totalpage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response totalPage(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {

            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserListEntity.class);
            RequestEntity<UserListEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            // 検索条件での検索件数を取得する
            int pageCount = model.getUserPageCount(instance.getRequestData());

            // 結果を返す
            ResponseEntity<Integer> result = null;
            if(pageCount >= 0){
                result = new ResponseEntity<Integer>(ResponseEntity.Result.OK,"",pageCount);

            }else{
                result = new ResponseEntity<Integer>(ResponseEntity.Result.NG,getMessage(MessagesConst.WarnCodes.SEARCH_RESULT_ZERO),pageCount);
            }

            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ユーザー一覧取得(ページング用)
     * @param json ログインユーザーIDと検索条件
     * @return レスポンス
     */
    @POST
    @Path("page")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userlistPage(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {

            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserListEntity.class);
            RequestEntity<UserListEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            // 検索条件での検索結果を取得する
            List<UserEntity> entities = model.getUsers(instance.getRequestData());

            // 結果を返す
            ResponseEntity<List<UserEntity>> result = null;
            result = new ResponseEntity<List<UserEntity>>(ResponseEntity.Result.OK,"",entities);

            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ユーザー一覧取得(全ページ)
     * @param json ログインユーザーIDと検索条件
     * @return レスポンス
     */
    @POST
    @Path("pages")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userlistPages(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {

            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserListEntity.class);
            RequestEntity<UserListEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            // 検索条件での検索結果を取得する
            List<UserEntity> entities = model.getAllUsers(instance.getRequestData());

            // 結果を返す
            ResponseEntity<List<UserEntity>> result = null;
            result = new ResponseEntity<List<UserEntity>>(ResponseEntity.Result.OK,"",entities);

            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ユーザー情報の検索
     * @param json ログインユーザーIDと変更情報
     * @return レスポンス
     */
    @POST
    @Path("find")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(String json) {

        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {

            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserEntity.class);
            RequestEntity<UserEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            // ユーザーを検索
            Optional<UserEntity> entity = model.getUser(instance.getRequestData().getId());

            // 結果を返す
            ResponseEntity<UserEntity> result = entity.map(data -> {
                return new ResponseEntity<UserEntity>(ResponseEntity.Result.OK,"",entity.get());
            }).orElse(new ResponseEntity<UserEntity>(ResponseEntity.Result.NG,getMessage(MessagesConst.ErrorCodes.NOT_FOUND),null));

            return Response.ok(mapper.writeValueAsString(result)).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    @POST
    @Path("download")
    @Produces(MediaType.TEXT_PLAIN)
    public Response download(@FormParam("json") String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {
            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserListEntity.class);
            RequestEntity<UserListEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            // DBからデータ取得
            List<UserEntity> entities = new ArrayList<>();
            entities = model.getAllUsers(instance.getRequestData());

            // CSV出力対象設定
            CsvEntity<UserEntity> entity = new CsvEntity<>(
                    Arrays.asList("getId","getName","getPassword","getDate","getTime","getTs"),
                    entities);

            // サンプルのファイル名
            String fileName = "テスト_" + session.getAttribute("userName") + ".csv";

            // 結果を返す
            return Response.ok(entity)
                    .header("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"))
                    .build();

        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    @POST
    @Path("downloadHeaderCSV")
    @Produces(MediaType.TEXT_PLAIN)
    public Response downloadHeaderCSV(@FormParam("json") String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel()) {
            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserListEntity.class);
            RequestEntity<UserListEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            // DBからデータ取得
            List<UserEntity> entities = new ArrayList<>();
            entities = model.getAllUsers(instance.getRequestData());

            // CSV出力対象設定
            CsvEntity<UserEntity> entity = new CsvEntity<>(
                    Arrays.asList("getId","getName","getPassword","getDate","getTime","getTs"),
                    Arrays.asList("ID","名称","パスワード","誕生日","時刻","日時"),
                    entities);

            // サンプルのファイル名
            String fileName = "テスト_" + session.getAttribute("userName") + ".csv";

            // 結果を返す
            return Response.ok(entity)
                    .header("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"))
                    .build();

        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    @POST
    @Path("downloadCSV")
    @Produces(MediaType.TEXT_PLAIN)
    public Response downloadCSV(@FormParam("json") String json) {
        ObjectMapper mapper = new ObjectMapper();

        RequestEntity<UserListEntity> instance;

        try {
            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserListEntity.class);

            instance = mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }

        //認証チェック（認証エラー時は401例外を出す）
        authCheck(instance.getLoginUserId());

        //ストリーミング処理
        StreamingOutput stream = getDownloadStreamingOutput(instance.getRequestData());

        // サンプルのファイル名
        String fileName = "テスト_" + session.getAttribute("userName") + ".csv";

        // 結果を返す
        try {
            return Response.ok(stream)
                    .header("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * ダウンロード用ストリーム取得
     * @param entity 画面入力値
     * @return ダウンロード用ストリーム
     */
    private StreamingOutput getDownloadStreamingOutput(UserListEntity entity) {
        return new StreamingOutput() {
            //書き込み
            @Override
            public void write(OutputStream out)
                    throws IOException, WebApplicationException {

                // BOM書き込み
                out.write( 0xef );
                out.write( 0xbb );
                out.write( 0xbf );

                try (UserModel model = new UserModel();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));) {
                    model.writeAllUsersCsv(entity, writer, new StandardCsvFormatter());
                } catch (Exception e) {
                    if(!ExcludedExceptionUtil.isClientAbortException(e)){
                        logger.error(e.getMessage());
                    }
                }
            }
        };
    }

    @POST
    @Path("downloadPDF")
    @Produces("application/pdf")
    public Response downloadPDF(@FormParam("json") String json) {
        ObjectMapper mapper = new ObjectMapper();

        try (UserModel model = new UserModel(); PdfUtil pdfUtil = new PdfUtil();) {
            // json文字列をUserDataにデシリアライズする
            JavaType type = mapper.getTypeFactory().constructParametricType(RequestEntity.class, UserListEntity.class);
            RequestEntity<UserListEntity> instance = mapper.readValue(json, type);

            //認証チェック（認証エラー時は401例外を出す）
            authCheck(instance.getLoginUserId());

            // DBからデータ取得
            List<UserEntity> entities = new ArrayList<>();
            entities = model.getUsers(new UserListEntity(instance.getRequestData().getSearchUserId()));

            // PDFのデータソースとして設定
            JRDataSource dataSource = new JRBeanCollectionDataSource(entities);

            Map<String, Object> params = new HashMap<>();

            // PDFの動的作成と取得
            byte[] result = pdfUtil.getPdfBytes("UserList", params, dataSource);

            // サンプルのファイル名
            String fileName = "テスト_" + session.getAttribute("userName") + ".pdf";

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
