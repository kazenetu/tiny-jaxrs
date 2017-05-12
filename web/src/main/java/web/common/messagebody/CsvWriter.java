package web.common.messagebody;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import web.common.base.CsvEntity;

@Provider
@Produces(MediaType.TEXT_PLAIN)
public class CsvWriter<E> implements MessageBodyWriter<CsvEntity<E>> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(CsvEntity<E> csvEntity, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        // サイズは気にしない
        return -1;
    }

    @Override
    public void writeTo(CsvEntity<E> csvEntity, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {

        // カラム指定がない場合は終了
        if (!csvEntity.isExitstColumns()) {
            return;
        }

        // 出力対象のカラムを取得
        List<String> columns = csvEntity.getColumns();

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(entityStream, Charset.forName("Windows-31J")));
                CSVPrinter printer = new CSVPrinter(writer, CSVFormat.RFC4180.withQuoteMode(QuoteMode.NON_NUMERIC));) {

            // getterメソッドを取得
            Class<?> entityClass = csvEntity.getCsvData().get(0).getClass();
            Map<String,Method> accessors = Stream.of(entityClass.getMethods())
                    .filter(m -> m.getReturnType() != void.class && m.getDeclaringClass() == entityClass && columns.contains(m.getName()))
                    .collect(Collectors.toMap(Method::getName, m->m));

            // レコードを出力
            for (E entity : csvEntity.getCsvData()) {
                for(String column : columns){
                    Method accessor = accessors.get(column);
                    printer.print(accessor.invoke(entity));
                }
                printer.println();
            }

        } catch (IllegalAccessException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

}