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

@Provider
@Produces(MediaType.TEXT_PLAIN)
public class CsvWriter<E> implements MessageBodyWriter<List<E>> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(List<E> entities, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        // サイズは気にしない
        return -1;
    }

    @Override
    public void writeTo(List<E> entities, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        if (entities.isEmpty()) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(entityStream, Charset.forName("Windows-31J")));
                CSVPrinter printer = new CSVPrinter(writer, CSVFormat.RFC4180.withQuoteMode(QuoteMode.NON_NUMERIC));) {

            Class<?> entityClass = entities.get(0).getClass();
            List<Method> accessors = Stream.of(entityClass.getMethods())
                    .filter(m -> m.getReturnType() != void.class && m.getDeclaringClass() == entityClass)
                    .collect(Collectors.toList());

            for (E entity : entities) {
                for (Method accessor : accessors) {
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