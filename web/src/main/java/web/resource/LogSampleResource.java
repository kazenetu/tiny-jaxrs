package web.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

@Path("log")
public class LogSampleResource {
	private static Logger logger = LoggerFactory.getLogger(LogSampleResource.class);

    @GET
    @Path("test1")
    @Produces(MediaType.TEXT_PLAIN)
	public String test1() {
    	ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger)logger;
        log.setLevel(Level.TRACE); // ★デフォルトだと trace レベルは出力されないので、出力のレベルを TRACE にしている

        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");

        return "test";
    }
}
